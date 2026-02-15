package pe.edu.cibertec.mscitas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI citasOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8082");
        localServer.setDescription("Servidor Local");

        Contact contact = new Contact();
        contact.setName("Equipo Visum Óptica");
        contact.setEmail("soporte@visumoptica.com");

        Info info = new Info()
                .title("API MS-CITAS - Visum Óptica")
                .version("1.0.0")
                .description("Microservicio de gestión de citas y sucursales con eventos asíncronos mediante RabbitMQ")
                .contact(contact)
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}