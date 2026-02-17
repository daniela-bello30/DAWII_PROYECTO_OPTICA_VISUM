package pe.edu.cibertec.msseguridad.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.msseguridad.dto.requests.LoginRequest;
import pe.edu.cibertec.msseguridad.dto.requests.RefreshTokenRequest;
import pe.edu.cibertec.msseguridad.dto.requests.RegisterRequest;
import pe.edu.cibertec.msseguridad.dto.requests.ValidateTokenRequest;
import pe.edu.cibertec.msseguridad.dto.response.ApiResponse;
import pe.edu.cibertec.msseguridad.dto.response.JwtResponse;
import pe.edu.cibertec.msseguridad.dto.response.UsuarioResponse;
import pe.edu.cibertec.msseguridad.dto.response.ValidateTokenResponse;
import pe.edu.cibertec.msseguridad.service.interfaces.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UsuarioResponse>> registrar(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /auth/register - Registrar nuevo usuario");
        UsuarioResponse usuarioResponse = authService.registrar(request);
        return new ResponseEntity<>(
                ApiResponse.success(usuarioResponse, "Usuario registrado exitosamente"),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /auth/login - Iniciar sesión con JWT");
        JwtResponse jwtResponse = authService.loginWithJwt(request);
        return ResponseEntity.ok(ApiResponse.success(jwtResponse, "Login exitoso"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("POST /auth/refresh - Refrescar token");
        JwtResponse jwtResponse = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success(jwtResponse, "Token refrescado exitosamente"));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<ApiResponse<ValidateTokenResponse>> validateToken(
            @Valid @RequestBody ValidateTokenRequest request) {
        log.info("POST /auth/validate-token - Validar token");
        ValidateTokenResponse response = authService.validateToken(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Token validado"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        log.info("POST /auth/logout - Cerrar sesión");
        // Extraer el token (quitar "Bearer ")
        String jwtToken = token.substring(7);
        authService.logout(jwtToken);
        return ResponseEntity.ok(ApiResponse.success(null, "Logout exitoso"));
    }
}