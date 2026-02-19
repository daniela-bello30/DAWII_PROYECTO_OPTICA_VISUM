package pe.edu.cibertec.catalogo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.catalogo.events.ProductoEvent;
import pe.edu.cibertec.catalogo.events.ProductoEventType;
import pe.edu.cibertec.catalogo.model.Producto;
import pe.edu.cibertec.catalogo.publisher.ProductoEventPublisher;
import pe.edu.cibertec.catalogo.repository.ProductoRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoEventPublisher eventPublisher;

    public List<Producto> listar(){
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Integer id){
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto crear (Producto producto){

        Producto guardado = productoRepository.save(producto);

        ProductoEvent event = new ProductoEvent();
        event.setTipo(ProductoEventType.CREATED);
        event.setProductoId(guardado.getIdProducto().longValue());
        event.setNombre(guardado.getNombre());
        event.setPrecio(guardado.getPrecio());
        event.setFecha(LocalDateTime.now());

        eventPublisher.publicar(event);

        return guardado;
    }

    public Producto actualizar (Integer id, Producto producto){

        Producto existente = buscarPorId(id);

        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());

        Producto actualizado = productoRepository.save(existente);

        ProductoEvent event = new ProductoEvent();
        event.setTipo(ProductoEventType.UPDATED);
        event.setProductoId(actualizado.getIdProducto().longValue());
        event.setNombre(actualizado.getNombre());
        event.setPrecio(actualizado.getPrecio());
        event.setFecha(LocalDateTime.now());

        eventPublisher.publicar(event);

        return actualizado;
    }

    public void eliminar (Integer id){
        productoRepository.deleteById(id);
    }
}