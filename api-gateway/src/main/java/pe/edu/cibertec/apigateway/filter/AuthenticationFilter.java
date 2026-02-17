package pe.edu.cibertec.apigateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pe.edu.cibertec.apigateway.service.JwtValidationService;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter
        extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtValidationService jwtValidationService;

    // Rutas que NO necesitan JWT
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/refresh"
    );

    public AuthenticationFilter(JwtValidationService jwtValidationService) {
        super(Config.class);
        this.jwtValidationService = jwtValidationService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            log.info("Gateway recibió petición: {} {}", request.getMethod(), path);

            // Verificar si la ruta es pública
            if (isPublicEndpoint(path)) {
                log.debug("Ruta pública, sin validación JWT: {}", path);
                return chain.filter(exchange);
            }

            // Verificar que exista el header Authorization
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                log.warn("Petición sin header Authorization: {}", path);
                return onError(exchange, "Header Authorization requerido", HttpStatus.UNAUTHORIZED);
            }

            // Extraer el token del header
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Formato de token inválido en: {}", path);
                return onError(exchange, "Token debe comenzar con 'Bearer '", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            // Validar el token localmente
            if (!jwtValidationService.isTokenValid(token)) {
                log.warn("Token inválido o expirado para ruta: {}", path);
                return onError(exchange, "Token inválido o expirado", HttpStatus.UNAUTHORIZED);
            }

            // Extraer información del token y agregar headers
            try {
                String email = jwtValidationService.extractUsername(token);
                Long userId = jwtValidationService.extractUserId(token);
                String role = jwtValidationService.extractRole(token);

                log.info("Token válido para usuario: {} (rol: {})", email, role);

                // Agregar headers con info del usuario para los microservicios
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-User-Email", email)
                        .header("X-User-Id", String.valueOf(userId))
                        .header("X-User-Role", role)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            } catch (Exception e) {
                log.error("Error extrayendo claims del token: {}", e.getMessage());
                return onError(exchange, "Error procesando token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    /**
     * Verifica si la ruta es pública (no requiere JWT).
     */
    private boolean isPublicEndpoint(String path) {
        return PUBLIC_ENDPOINTS.stream()
                .anyMatch(path::endsWith);
    }

    /**
     * Retorna respuesta de error al cliente.
     */
    private Mono<Void> onError(ServerWebExchange exchange,
                               String message,
                               HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");

        String body = String.format(
                "{\"success\":false,\"message\":\"%s\",\"status\":%d}",
                message, status.value()
        );

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes()))
        );
    }

    public static class Config {
        // Sin configuración adicional por ahora
    }
}
