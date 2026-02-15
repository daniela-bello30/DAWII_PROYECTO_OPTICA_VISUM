package pe.edu.cibertec.mscitas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients; // Agrega esto

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients // Agrega esto
public class MsCitasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsCitasApplication.class, args);
    }
}