package pe.edu.cibertec.msventas.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.msventas.dto.ProductoDTO;

@Slf4j
@Component
public class CatalogoFallback implements CatalogoFeignClient {

    @Override
    public ProductoDTO obtenerProducto(Integer id) {
        log.error("⚠️ FALLBACK: ms-catalogo no disponible - obtenerProducto({})", id);
        throw new RuntimeException("El servicio de catálogo no está disponible");
    }

    @Override
    public Boolean verificarStock(Integer id, Integer cantidad) {
        log.error("⚠️ FALLBACK: ms-catalogo no disponible - verificarStock({}, {})", id, cantidad);
        throw new RuntimeException("No se puede verificar el stock en este momento");
    }

    @Override
    public void reducirStock(Integer id, Integer cantidad) {
        log.error("⚠️ FALLBACK: ms-catalogo no disponible - reducirStock({}, {})", id, cantidad);
        throw new RuntimeException("No se puede reducir el stock en este momento");
    }

    @Override
    public void aumentarStock(Integer id, Integer cantidad) {
        log.error("⚠️ FALLBACK: ms-catalogo no disponible - aumentarStock({}, {})", id, cantidad);
        log.warn("⚠️ Stock no restaurado automáticamente. Revisar manualmente.");
    }
}