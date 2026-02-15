package pe.edu.cibertec.msventas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API MS-VENTAS - Visum Óptica")
                        .version("1.0.0")
                        .description("Microservicio de Ventas con OpenFeign y RabbitMQ")
                        .contact(new Contact()
                                .name("Integrante 4 - Luis")
                                .email("luis@visumoptica.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8082")
                                .description("Servidor Local Directo"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Via API Gateway")
                ));
    }
}