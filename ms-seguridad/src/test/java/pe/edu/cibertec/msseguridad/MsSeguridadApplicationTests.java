package pe.edu.cibertec.msseguridad;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class MsSeguridadApplicationTests {

    @Test
    void generarHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("admin123");

        System.out.println("================================");
        System.out.println("HASH_GENERADO:");
        System.out.println(hash);
        System.out.println("================================");
    }
}
