package pe.edu.cibertec.msseguridad.security;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.msseguridad.service.interfaces.JwtService;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtService jwtService;

    /**
     * Extrae el ID del usuario del token
     */
    public Long extractUserId(String token) {
        Claims claims = jwtService.extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Extrae el rol del usuario del token
     */
    public String extractUserRole(String token) {
        Claims claims = jwtService.extractAllClaims(token);
        return claims.get("rol", String.class);
    }

    /**
     * Valida si el token contiene la información necesaria
     */
    public boolean validateTokenClaims(String token) {
        try {
            Claims claims = jwtService.extractAllClaims(token);
            return claims.get("userId") != null &&
                    claims.get("rol") != null &&
                    claims.getSubject() != null;
        } catch (Exception e) {
            log.error("Error validando claims del token: {}", e.getMessage());
            return false;
        }
    }
}
