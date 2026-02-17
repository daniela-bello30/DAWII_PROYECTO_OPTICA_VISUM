package pe.edu.cibertec.apigateway.filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("GATEWAY REQUEST");
        log.info("  Método  : {}", request.getMethod());
        log.info("  URI     : {}", request.getURI());
        log.info("  Headers : Authorization={}",
                request.getHeaders().containsKey("Authorization") ? "[PRESENTE]" : "[AUSENTE]");
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            log.info("GATEWAY RESPONSE → Status: {}", response.getStatusCode());
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Ejecutar antes que otros filtros
    }
}
