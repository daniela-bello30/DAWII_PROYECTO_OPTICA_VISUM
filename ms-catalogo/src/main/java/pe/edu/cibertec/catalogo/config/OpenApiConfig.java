package pe.edu.cibertec.catalogo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-CATALOGO API")
                        .version("1.0.0")
                        .description("""
                                # Microservicio de Catálogo - Óptica
                                
                                API REST para la gestión completa del catálogo de productos de la óptica.
                                
                                ## Funcionalidades principales:
                                * ✅ Gestión de Productos (CRUD completo)
                                * ✅ Gestión de Marcas
                                * ✅ Gestión de Categorías
                                * ✅ Gestión de Imágenes de Productos
                                * ✅ Búsquedas avanzadas y filtros
                                * ✅ Control de stock
                                * ✅ Integración con RabbitMQ (eventos)
                                * ✅ Preparado para OpenFeign
                                
                                ## Arquitectura:
                                * Spring Boot 3.2.5
                                * Spring Cloud 2023.0.1
                                * MySQL Database
                                * RabbitMQ Messaging
                                * Eureka Service Discovery
                                """)
                        .contact(new Contact()
                                .name("Integrante 3 - Daniela")
                                .email("daniela@cibertec.edu.pe")
                                .url("https://github.com/OpticaMicroservices/ms-catalogo"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor Local"),
                        new Server()
                                .url("http://localhost:8080/catalogo")
                                .description("A través del API Gateway")
                ));
    }
}