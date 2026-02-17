package pe.edu.cibertec.msseguridad.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.msseguridad.dto.events.UsuarioActualizadoEvent;
import pe.edu.cibertec.msseguridad.dto.requests.ActualizarUsuarioRequest;
import pe.edu.cibertec.msseguridad.dto.response.UsuarioResponse;
import pe.edu.cibertec.msseguridad.exception.BusinessException;
import pe.edu.cibertec.msseguridad.exception.ResourceNotFoundException;
import pe.edu.cibertec.msseguridad.model.Rol;
import pe.edu.cibertec.msseguridad.model.Usuario;
import pe.edu.cibertec.msseguridad.repository.RolRepository;
import pe.edu.cibertec.msseguridad.repository.UsuarioRepository;
import pe.edu.cibertec.msseguridad.service.interfaces.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ModelMapper modelMapper;
    private final EventPublisherService eventPublisherService;

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuarioPorId(Long idUsuario) {
        log.info("Obteniendo usuario con ID: {}", idUsuario);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));

        return mapearUsuarioAResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuarios() {
        log.info("Listando todos los usuarios");

        return usuarioRepository.findAll().stream()
                .map(this::mapearUsuarioAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuariosActivos() {
        log.info("Listando usuarios activos");

        return usuarioRepository.findByEstado(true).stream()
                .map(this::mapearUsuarioAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioResponse actualizarUsuario(Long idUsuario, ActualizarUsuarioRequest request) {
        log.info("Actualizando usuario con ID: {}", idUsuario);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));

        // Validar email único si se está cambiando
        if (request.getEmail() != null && !request.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException("El email ya está registrado");
            }
            usuario.setEmail(request.getEmail());
        }

        // Actualizar otros campos si están presentes
        if (request.getNombres() != null) {
            usuario.setNombres(request.getNombres());
        }
        if (request.getApellidos() != null) {
            usuario.setApellidos(request.getApellidos());
        }
        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }
        if (request.getDocumentoIdentidad() != null) {
            if (!request.getDocumentoIdentidad().equals(usuario.getDocumentoIdentidad()) &&
                    usuarioRepository.existsByDocumentoIdentidad(request.getDocumentoIdentidad())) {
                throw new BusinessException("El documento de identidad ya está registrado");
            }
            usuario.setDocumentoIdentidad(request.getDocumentoIdentidad());
        }
        if (request.getTipoDocumento() != null) {
            usuario.setTipoDocumento(request.getTipoDocumento());
        }
        if (request.getFechaNacimiento() != null) {
            usuario.setFechaNacimiento(request.getFechaNacimiento());
        }
        if (request.getIdRol() != null) {
            Rol rol = rolRepository.findById(request.getIdRol())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", request.getIdRol()));
            usuario.setRol(rol);
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        log.info("Usuario actualizado exitosamente");

        // Publicar evento
        UsuarioActualizadoEvent evento = UsuarioActualizadoEvent.builder()
                .idUsuario(usuarioActualizado.getIdUsuario())
                .email(usuarioActualizado.getEmail())
                .telefono(usuarioActualizado.getTelefono())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        eventPublisherService.publicarUsuarioActualizado(evento);

        return mapearUsuarioAResponse(usuarioActualizado);
    }

    @Override
    @Transactional
    public void desactivarUsuario(Long idUsuario) {
        log.info("Desactivando usuario con ID: {}", idUsuario);

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", idUsuario));

        usuario.setEstado(false);
        usuarioRepository.save(usuario);

        log.info("Usuario desactivado exitosamente");
    }

    private UsuarioResponse mapearUsuarioAResponse(Usuario usuario) {
        UsuarioResponse response = modelMapper.map(usuario, UsuarioResponse.class);
        response.setNombreRol(usuario.getRol().getNombreRol());
        response.setIdRol(usuario.getRol().getIdRol());
        return response;
    }
}
