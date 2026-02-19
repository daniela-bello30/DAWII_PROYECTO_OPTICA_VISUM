package pe.edu.cibertec.catalogo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.catalogo.model.Producto;
import pe.edu.cibertec.catalogo.publisher.ProductoEventPublisher;
import pe.edu.cibertec.catalogo.repository.ProductoRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoEventPublisher eventPublisher;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void debeCrearProductoYPublicarEvento() {

        Producto producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombre("Lentes");
        producto.setPrecio(new BigDecimal("120.00"));

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(producto);

        Producto resultado = productoService.crear(producto);

        assertNotNull(resultado);
        assertEquals("Lentes", resultado.getNombre());

        // ⭐ verificar evento
        verify(eventPublisher, times(1))
                .publicar(any());
    }
}