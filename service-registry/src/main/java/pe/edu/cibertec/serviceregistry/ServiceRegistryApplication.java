package pe.edu.cibertec.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer  // ← FALTA ESTA ANOTACIÓN
public class ServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
        System.out.println("========================================");
        System.out.println("✅ EUREKA SERVER INICIADO");
        System.out.println("📡 Puerto: 8761");
        System.out.println("🌐 Dashboard: http://localhost:8761");
        System.out.println("========================================");
    }
}