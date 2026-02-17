package pe.edu.cibertec.msventas.feign;

import pe.edu.cibertec.msventas.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-catalogo", fallback = CatalogoFallback.class)
public interface CatalogoFeignClient {

    @GetMapping("/api/productos/{id}")
    ProductoDTO obtenerProducto(@PathVariable("id") Integer id);

    @GetMapping("/api/productos/{id}/verificar-stock")
    Boolean verificarStock(
            @PathVariable("id") Integer id,
            @RequestParam("cantidad") Integer cantidad
    );

    @PatchMapping("/api/productos/{id}/reducir-stock")
    void reducirStock(
            @PathVariable("id") Integer id,
            @RequestParam("cantidad") Integer cantidad
    );

    @PatchMapping("/api/productos/{id}/aumentar-stock")
    void aumentarStock(
            @PathVariable("id") Integer id,
            @RequestParam("cantidad") Integer cantidad
    );
}