package pe.edu.cibertec.msventas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MsVentasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsVentasApplication.class, args);

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🚀 MS-VENTAS iniciado correctamente");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("📚 Swagger:  http://localhost:8082/swagger-ui.html");
        System.out.println("🌐 Gateway:  http://localhost:8080/api/ventas");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🔄 Feign → ms-catalogo (8081)");
        System.out.println("📡 RabbitMQ → optica.ventas.queue");
        System.out.println("═══════════════════════════════════════════════════════");
    }
}
