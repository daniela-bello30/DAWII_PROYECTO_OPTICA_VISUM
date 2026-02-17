package pe.edu.cibertec.msseguridad.service.interfaces;


import pe.edu.cibertec.msseguridad.dto.requests.LoginRequest;
import pe.edu.cibertec.msseguridad.dto.requests.RefreshTokenRequest;
import pe.edu.cibertec.msseguridad.dto.requests.RegisterRequest;
import pe.edu.cibertec.msseguridad.dto.requests.ValidateTokenRequest;
import pe.edu.cibertec.msseguridad.dto.response.JwtResponse;
import pe.edu.cibertec.msseguridad.dto.response.UsuarioResponse;
import pe.edu.cibertec.msseguridad.dto.response.ValidateTokenResponse;

public interface AuthService {

    UsuarioResponse registrar(RegisterRequest request);

    // NUEVO: Login con JWT
    JwtResponse loginWithJwt(LoginRequest request);

    // NUEVO: Refrescar token
    JwtResponse refreshToken(RefreshTokenRequest request);

    // NUEVO: Validar token
    ValidateTokenResponse validateToken(ValidateTokenRequest request);

    // NUEVO: Logout (opcional, para blacklist de tokens)
    void logout(String token);
}
