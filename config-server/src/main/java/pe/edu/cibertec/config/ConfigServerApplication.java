package pe.edu.cibertec.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
        System.out.println("========================================");
        System.out.println("✅ CONFIG SERVER INICIADO");
        System.out.println("📡 Puerto: 8888");
        System.out.println("🔍 Eureka: Registrado como 'config-server'");
        System.out.println("📁 Configuraciones: Servidas desde repositorio");
        System.out.println("========================================");
    }
}