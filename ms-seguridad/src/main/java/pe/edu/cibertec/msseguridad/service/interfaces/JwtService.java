package pe.edu.cibertec.msseguridad.service.interfaces;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    /**
     * Genera un token JWT para el usuario
     */
    String generateToken(String username, Long userId, String rol);

    /**
     * Genera un token con claims adicionales
     */
    String generateToken(Map<String, Object> extraClaims, String username);

    /**
     * Genera un refresh token
     */
    String generateRefreshToken(String username);

    /**
     * Extrae el username del token
     */
    String extractUsername(String token);

    /**
     * Extrae un claim específico del token
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Extrae todos los claims del token
     */
    Claims extractAllClaims(String token);

    /**
     * Valida si el token es válido para el usuario
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Verifica si el token ha expirado
     */
    boolean isTokenExpired(String token);

    /**
     * Extrae la fecha de expiración del token
     */
    Date extractExpiration(String token);
}