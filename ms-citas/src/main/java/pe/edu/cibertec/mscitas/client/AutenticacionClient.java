package pe.edu.cibertec.mscitas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.mscitas.dto.UsuarioDTO;

// El nombre debe ser el que Franchesco puso en su spring.application.name
@FeignClient(name = "ms-autenticacion")
public interface AutenticacionClient {

    @GetMapping("/usuarios/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable("id") Integer id);
}