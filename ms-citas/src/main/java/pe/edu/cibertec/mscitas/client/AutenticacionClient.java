package pe.edu.cibertec.mscitas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.mscitas.config.FeignInterceptorConfig;
import pe.edu.cibertec.mscitas.dto.ApiResponse;
import pe.edu.cibertec.mscitas.dto.UsuarioDTO;

// El nombre debe ser el que Franchesco puso en su spring.application.name
@FeignClient(name = "ms-seguridad", configuration = FeignInterceptorConfig.class)
public interface AutenticacionClient {

    @GetMapping("/usuarios/{id}")
    ApiResponse<UsuarioDTO> obtenerUsuario(@PathVariable("id") Integer id);
}