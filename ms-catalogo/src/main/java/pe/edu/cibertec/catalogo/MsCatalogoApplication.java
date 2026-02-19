package pe.edu.cibertec.catalogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MsCatalogoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCatalogoApplication.class, args);
        System.out.println("====================================");
        System.out.println("✅ MS-CATALOGO INICIADO CORRECTAMENTE");
        System.out.println("📦 Puerto: 8081");
        System.out.println("📚 Swagger UI: http://localhost:8081/swagger-ui.html");
        System.out.println("📄 API Docs: http://localhost:8081/api-docs");
        System.out.println("💚 Health: http://localhost:8081/actuator/health");
        System.out.println("🔍 Eureka: Registrado como 'ms-catalogo'");
        System.out.println("====================================");
    }
}