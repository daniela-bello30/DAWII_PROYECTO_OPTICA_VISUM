package pe.edu.cibertec.msventas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.msventas.dto.ProductoDTO;
import pe.edu.cibertec.msventas.feign.CatalogoFeignClient;
import pe.edu.cibertec.msventas.model.Carrito;
import pe.edu.cibertec.msventas.model.DetalleCarrito;
import pe.edu.cibertec.msventas.repository.CarritoRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CatalogoFeignClient catalogoClient;

    public Carrito obtenerCarritoPorUsuario(Integer idUsuario) {
        return carritoRepository.findByIdUsuarioAndEstado(idUsuario, "Activo")
                .orElseGet(() -> crearCarritoNuevo(idUsuario));
    }

    @Transactional
    public Carrito agregarItemAlCarrito(Integer idUsuario, Integer idProducto, Integer cantidad) {
        log.info("🛒 Agregando al carrito - Usuario: {}, Producto: {}, Cantidad: {}",
                idUsuario, idProducto, cantidad);

        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        // Verificar producto y stock
        ProductoDTO producto = catalogoClient.obtenerProducto(idProducto);
        Boolean hayStock = catalogoClient.verificarStock(idProducto, cantidad);

        if (!hayStock) {
            throw new RuntimeException("Stock insuficiente para: " + producto.getNombreProducto());
        }

        DetalleCarrito item = DetalleCarrito.builder()
                .carrito(carrito)
                .idProducto(idProducto)
                .cantidad(cantidad)
                .precioUnitario(producto.getPrecioUnitario())
                .build();

        item.calcularSubtotal();
        carrito.getItems().add(item);

        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito actualizarCantidadItem(Integer idUsuario, Integer idDetalle, Integer nuevaCantidad) {
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        DetalleCarrito item = carrito.getItems().stream()
                .filter(i -> i.getIdDetalle().equals(idDetalle))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        Boolean hayStock = catalogoClient.verificarStock(item.getIdProducto(), nuevaCantidad);
        if (!hayStock) {
            throw new RuntimeException("Stock insuficiente");
        }

        item.setCantidad(nuevaCantidad);
        item.calcularSubtotal();

        return carritoRepository.save(carrito);
    }

    @Transactional
    public void eliminarItemDelCarrito(Integer idUsuario, Integer idDetalle) {
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);
        carrito.getItems().removeIf(item -> item.getIdDetalle().equals(idDetalle));
        carritoRepository.save(carrito);
    }

    @Transactional
    public void vaciarCarrito(Integer idUsuario) {
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);
        carrito.getItems().clear();
        carritoRepository.save(carrito);
    }

    private Carrito crearCarritoNuevo(Integer idUsuario) {
        Carrito carrito = Carrito.builder()
                .idUsuario(idUsuario)
                .estado("Activo")
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        return carritoRepository.save(carrito);
    }
}