package pe.edu.cibertec.msseguridad.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.msseguridad.dto.events.UsuarioRegistradoEvent;
import pe.edu.cibertec.msseguridad.dto.requests.LoginRequest;
import pe.edu.cibertec.msseguridad.dto.requests.RefreshTokenRequest;
import pe.edu.cibertec.msseguridad.dto.requests.RegisterRequest;
import pe.edu.cibertec.msseguridad.dto.requests.ValidateTokenRequest;
import pe.edu.cibertec.msseguridad.dto.response.JwtResponse;
import pe.edu.cibertec.msseguridad.dto.response.UsuarioResponse;
import pe.edu.cibertec.msseguridad.dto.response.ValidateTokenResponse;
import pe.edu.cibertec.msseguridad.exception.EmailAlreadyExistsException;
import pe.edu.cibertec.msseguridad.exception.InvalidCredentialsException;
import pe.edu.cibertec.msseguridad.exception.ResourceNotFoundException;
import pe.edu.cibertec.msseguridad.model.Rol;
import pe.edu.cibertec.msseguridad.model.Usuario;
import pe.edu.cibertec.msseguridad.repository.RolRepository;
import pe.edu.cibertec.msseguridad.repository.UsuarioRepository;
import pe.edu.cibertec.msseguridad.security.JwtTokenProvider;
import pe.edu.cibertec.msseguridad.service.interfaces.AuthService;
import pe.edu.cibertec.msseguridad.service.interfaces.JwtService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EventPublisherService eventPublisherService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Override
    @Transactional
    public UsuarioResponse registrar(RegisterRequest request) {
        log.info("Iniciando registro de usuario: {}", request.getEmail());

        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        // Validar que el documento no exista
        if (usuarioRepository.existsByDocumentoIdentidad(request.getDocumentoIdentidad())) {
            throw new EmailAlreadyExistsException("El documento de identidad ya está registrado");
        }

        // Obtener el rol
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", request.getIdRol()));

        // Encriptar contraseña
        String passwordEncriptada = passwordEncoder.encode(request.getPassword());

        // Crear usuario
        Usuario usuario = Usuario.builder()
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .password(passwordEncriptada)
                .telefono(request.getTelefono())
                .documentoIdentidad(request.getDocumentoIdentidad())
                .tipoDocumento(request.getTipoDocumento())
                .fechaNacimiento(request.getFechaNacimiento())
                .rol(rol)
                .estado(true)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente con ID: {}", usuarioGuardado.getIdUsuario());

        // Publicar evento
        UsuarioRegistradoEvent evento = UsuarioRegistradoEvent.builder()
                .idUsuario(usuarioGuardado.getIdUsuario())
                .nombres(usuarioGuardado.getNombres())
                .apellidos(usuarioGuardado.getApellidos())
                .email(usuarioGuardado.getEmail())
                .telefono(usuarioGuardado.getTelefono())
                .nombreRol(rol.getNombreRol())
                .fechaRegistro(usuarioGuardado.getFechaRegistro())
                .build();

        eventPublisherService.publicarUsuarioRegistrado(evento);

        // Mapear respuesta
        UsuarioResponse response = new UsuarioResponse();
        response.setIdUsuario(usuarioGuardado.getIdUsuario());
        response.setNombres(usuarioGuardado.getNombres());
        response.setApellidos(usuarioGuardado.getApellidos());
        response.setEmail(usuarioGuardado.getEmail());
        response.setTelefono(usuarioGuardado.getTelefono());
        response.setDocumentoIdentidad(usuarioGuardado.getDocumentoIdentidad());
        response.setTipoDocumento(usuarioGuardado.getTipoDocumento());
        response.setFechaNacimiento(usuarioGuardado.getFechaNacimiento());
        response.setNombreRol(rol.getNombreRol());
        response.setIdRol(rol.getIdRol());
        response.setEstado(usuarioGuardado.getEstado());
        response.setFechaRegistro(usuarioGuardado.getFechaRegistro());

        return response;
    }

    @Override
    @Transactional
    public JwtResponse loginWithJwt(LoginRequest request) {
        log.info("Intento de login con JWT para email: {}", request.getEmail());

        // ═══ AGREGADO TEMPORAL - DIAGNÓSTICO ═══
        Usuario usuarioTest = usuarioRepository.findByEmail(request.getEmail()).orElse(null);
        if (usuarioTest != null) {
            log.info("═══════════════════════════════════════");
            log.info("DIAGNÓSTICO COMPLETO:");
            log.info("Email recibido: '{}'", request.getEmail());
            log.info("Password recibido: '{}'", request.getPassword());
            log.info("Password BD: '{}'", usuarioTest.getPassword());
            log.info("Password BD longitud: {}", usuarioTest.getPassword().length());

            // PRUEBA MANUAL
            boolean manualMatch = passwordEncoder.matches(request.getPassword(), usuarioTest.getPassword());
            log.info("¿passwordEncoder.matches() manual = {}?", manualMatch);

            if (manualMatch) {
                log.info("✓✓✓ PASSWORD CORRECTO - El problema es Spring Security");
            } else {
                log.info("✗✗✗ PASSWORD INCORRECTO - El problema es el hash");
            }
            log.info("═══════════════════════════════════════");
        }
        
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            /*Mantenerlo temporalmente*/
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            /*mantenerlo temporalmente*/


            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException());


            if (!usuario.getEstado()) {
                throw new InvalidCredentialsException("Usuario inactivo");
            }


            String accessToken = jwtService.generateToken(
                    usuario.getEmail(),
                    usuario.getIdUsuario(),
                    usuario.getRol().getNombreRol()
            );

            String refreshToken = jwtService.generateRefreshToken(usuario.getEmail());


            usuario.setUltimoAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);

            log.info("Login con JWT exitoso para usuario: {}", usuario.getEmail());

            return JwtResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .type("Bearer")
                    .idUsuario(usuario.getIdUsuario())
                    .email(usuario.getEmail())
                    .nombres(usuario.getNombres())
                    .apellidos(usuario.getApellidos())
                    .rol(usuario.getRol().getNombreRol())
                    .expiresIn(jwtExpiration)
                    .build();

        } catch (Exception e) {
            log.error("Error en login: {}", e.getMessage());
            throw new InvalidCredentialsException();
        }
    }

    @Override
    @Transactional
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        log.info("Intentando refrescar token");

        try {
            String refreshToken = request.getRefreshToken();

            // Extraer email del refresh token
            String email = jwtService.extractUsername(refreshToken);

            // Buscar usuario
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", email));

            // Verificar que el usuario esté activo
            if (!usuario.getEstado()) {
                throw new InvalidCredentialsException("Usuario inactivo");
            }

            // Verificar que el refresh token no haya expirado
            if (jwtService.isTokenExpired(refreshToken)) {
                throw new InvalidCredentialsException("Refresh token expirado");
            }

            // Generar nuevos tokens
            String newAccessToken = jwtService.generateToken(
                    usuario.getEmail(),
                    usuario.getIdUsuario(),
                    usuario.getRol().getNombreRol()
            );

            String newRefreshToken = jwtService.generateRefreshToken(usuario.getEmail());

            log.info("Token refrescado exitosamente para usuario: {}", usuario.getEmail());

            return JwtResponse.builder()
                    .token(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .type("Bearer")
                    .idUsuario(usuario.getIdUsuario())
                    .email(usuario.getEmail())
                    .nombres(usuario.getNombres())
                    .apellidos(usuario.getApellidos())
                    .rol(usuario.getRol().getNombreRol())
                    .expiresIn(jwtExpiration)
                    .build();

        } catch (Exception e) {
            log.error("Error al refrescar token: {}", e.getMessage());
            throw new InvalidCredentialsException("Refresh token inválido");
        }
    }

    @Override
    public ValidateTokenResponse validateToken(ValidateTokenRequest request) {
        log.info("Validando token");

        try {
            String token = request.getToken();

            // Extraer información del token
            String email = jwtService.extractUsername(token);
            Long userId = jwtTokenProvider.extractUserId(token);
            String rol = jwtTokenProvider.extractUserRole(token);

            // Verificar que el token no haya expirado
            if (jwtService.isTokenExpired(token)) {
                return ValidateTokenResponse.builder()
                        .valid(false)
                        .message("Token expirado")
                        .build();
            }

            // Validar claims del token
            if (!jwtTokenProvider.validateTokenClaims(token)) {
                return ValidateTokenResponse.builder()
                        .valid(false)
                        .message("Token inválido - claims incorrectos")
                        .build();
            }

            log.info("Token validado exitosamente para usuario: {}", email);

            return ValidateTokenResponse.builder()
                    .valid(true)
                    .email(email)
                    .userId(userId)
                    .rol(rol)
                    .message("Token válido")
                    .build();

        } catch (Exception e) {
            log.error("Error al validar token: {}", e.getMessage());
            return ValidateTokenResponse.builder()
                    .valid(false)
                    .message("Token inválido: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public void logout(String token) {
        log.info("Logout - Token invalidado (implementar blacklist si es necesario)");
        // TODO: Implementar blacklist de tokens si es necesario
        // Por ahora, como los tokens son stateless, simplemente el cliente debe eliminar el token
    }
}
