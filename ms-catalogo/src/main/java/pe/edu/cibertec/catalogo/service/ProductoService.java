package pe.edu.cibertec.catalogo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.catalogo.dto.ProductoDTO;
import pe.edu.cibertec.catalogo.dto.ProductoRequestDTO;
import pe.edu.cibertec.catalogo.dto.ProductoSimpleDTO;
import pe.edu.cibertec.catalogo.events.ProductoEvent;
import pe.edu.cibertec.catalogo.events.ProductoEventType;
import pe.edu.cibertec.catalogo.exception.BusinessException;
import pe.edu.cibertec.catalogo.exception.ResourceNotFoundException;
import pe.edu.cibertec.catalogo.model.Categoria;
import pe.edu.cibertec.catalogo.model.Marca;
import pe.edu.cibertec.catalogo.model.Producto;
import pe.edu.cibertec.catalogo.publisher.ProductoEventPublisher;
import pe.edu.cibertec.catalogo.repository.CategoriaRepository;
import pe.edu.cibertec.catalogo.repository.MarcaRepository;
import pe.edu.cibertec.catalogo.repository.ProductoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;
    private final ProductoEventPublisher eventPublisher;

    // ============================
    // CONSULTAS
    // ============================

    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream()
                .map(this::toProductoDTO)
                .collect(Collectors.toList());
    }

    public Page<ProductoDTO> obtenerTodosPaginado(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return productoRepository.findAll(pageable).map(this::toProductoDTO);
    }

    public List<ProductoSimpleDTO> obtenerActivos() {
        return productoRepository.findByActivo(true).stream()
                .map(this::toProductoSimpleDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO obtenerPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        return toProductoDTO(producto);
    }

    public ProductoDTO obtenerPorSku(String sku) {
        Producto producto = productoRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "sku", sku));
        return toProductoDTO(producto);
    }

    public List<ProductoSimpleDTO> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toProductoSimpleDTO)
                .collect(Collectors.toList());
    }

    public Page<ProductoDTO> busquedaAvanzada(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.busquedaAvanzada(searchTerm, pageable).map(this::toProductoDTO);
    }

    public List<ProductoSimpleDTO> filtrarPorMarca(Integer idMarca) {
        return productoRepository.findByMarcaIdMarca(idMarca).stream()
                .map(this::toProductoSimpleDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoSimpleDTO> filtrarPorCategoria(Integer idCategoria) {
        return productoRepository.findByCategoria_IdCategoria(idCategoria).stream()
                .map(this::toProductoSimpleDTO)
                .collect(Collectors.toList());
    }

    public Page<ProductoDTO> filtrarProductos(Integer idMarca, Integer idCategoria,
                                               BigDecimal precioMin, BigDecimal precioMax,
                                               int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.filtrarProductos(idMarca, idCategoria, precioMin, precioMax, pageable)
                .map(this::toProductoDTO);
    }

    public List<ProductoSimpleDTO> obtenerProductosConStock() {
        return productoRepository.findProductosConStock().stream()
                .map(this::toProductoSimpleDTO)
                .collect(Collectors.toList());
    }

    public Page<ProductoDTO> obtenerProductosPopulares(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productoRepository.findProductosPopulares(pageable).map(this::toProductoDTO);
    }

    // ============================
    // CRUD
    // ============================

    public ProductoDTO crear(ProductoRequestDTO productoRequest) {
        if (productoRequest.getSku() != null && productoRepository.existsBySku(productoRequest.getSku())) {
            throw new BusinessException("Ya existe un producto con el SKU: " + productoRequest.getSku());
        }

        Marca marca = marcaRepository.findById(productoRequest.getIdMarca())
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", productoRequest.getIdMarca()));

        Categoria categoria = categoriaRepository.findById(productoRequest.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", productoRequest.getIdCategoria()));

        Producto producto = new Producto();
        producto.setNombre(productoRequest.getNombre());
        producto.setDescripcion(productoRequest.getDescripcion());
        producto.setPrecio(productoRequest.getPrecio());
        producto.setStock(productoRequest.getStock() != null ? productoRequest.getStock() : 0);
        producto.setSku(productoRequest.getSku());
        producto.setMarca(marca);
        producto.setCategoria(categoria);
        producto.setActivo(productoRequest.getActivo() != null ? productoRequest.getActivo() : true);

        Producto guardado = productoRepository.save(producto);

        ProductoEvent event = new ProductoEvent();
        event.setTipo(ProductoEventType.CREATED);
        event.setProductoId(guardado.getIdProducto().longValue());
        event.setNombre(guardado.getNombre());
        event.setPrecio(guardado.getPrecio());
        event.setFecha(LocalDateTime.now());
        eventPublisher.publicar(event);

        return toProductoDTO(guardado);
    }

    public ProductoDTO actualizar(Integer id, ProductoRequestDTO productoRequest) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        if (productoRequest.getSku() != null
                && !productoRequest.getSku().equals(existente.getSku())
                && productoRepository.existsBySku(productoRequest.getSku())) {
            throw new BusinessException("Ya existe otro producto con el SKU: " + productoRequest.getSku());
        }

        existente.setNombre(productoRequest.getNombre());
        existente.setDescripcion(productoRequest.getDescripcion());
        existente.setPrecio(productoRequest.getPrecio());
        existente.setStock(productoRequest.getStock());
        existente.setSku(productoRequest.getSku());

        if (productoRequest.getIdMarca() != null) {
            Marca marca = marcaRepository.findById(productoRequest.getIdMarca())
                    .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", productoRequest.getIdMarca()));
            existente.setMarca(marca);
        }

        if (productoRequest.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(productoRequest.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", productoRequest.getIdCategoria()));
            existente.setCategoria(categoria);
        }

        if (productoRequest.getActivo() != null) {
            existente.setActivo(productoRequest.getActivo());
        }

        Producto actualizado = productoRepository.save(existente);

        ProductoEvent event = new ProductoEvent();
        event.setTipo(ProductoEventType.UPDATED);
        event.setProductoId(actualizado.getIdProducto().longValue());
        event.setNombre(actualizado.getNombre());
        event.setPrecio(actualizado.getPrecio());
        event.setFecha(LocalDateTime.now());
        eventPublisher.publicar(event);

        return toProductoDTO(actualizado);
    }

    public void eliminar(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public ProductoDTO reactivar(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        producto.setActivo(true);
        return toProductoDTO(productoRepository.save(producto));
    }

    // ============================
    // STOCK
    // ============================

    public boolean verificarStock(Integer id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        return producto.tieneStock(cantidad);
    }

    public void reducirStock(Integer id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        producto.reducirStock(cantidad);
        productoRepository.save(producto);
    }

    public void aumentarStock(Integer id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        producto.aumentarStock(cantidad);
        productoRepository.save(producto);
    }

    public ProductoDTO actualizarStock(Integer id, Integer nuevoStock) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
        if (nuevoStock < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }
        producto.setStock(nuevoStock);
        return toProductoDTO(productoRepository.save(producto));
    }

    // ============================
    // HELPERS DE MAPEO
    // ============================

    private ProductoDTO toProductoDTO(Producto p) {
        ProductoDTO dto = modelMapper.map(p, ProductoDTO.class);
        if (p.getMarca() != null) {
            dto.setNombreMarca(p.getMarca().getNombre());
        }
        if (p.getCategoria() != null) {
            dto.setNombreCategoria(p.getCategoria().getNombre());
        }
        dto.setDisponible(p.getStock() != null && p.getStock() > 0 && Boolean.TRUE.equals(p.getActivo()));
        return dto;
    }

    private ProductoSimpleDTO toProductoSimpleDTO(Producto p) {
        ProductoSimpleDTO dto = new ProductoSimpleDTO();
        dto.setIdProducto(p.getIdProducto());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setSku(p.getSku());
        dto.setActivo(p.getActivo());
        dto.setDisponible(p.getStock() != null && p.getStock() > 0 && Boolean.TRUE.equals(p.getActivo()));
        if (p.getMarca() != null) {
            dto.setNombreMarca(p.getMarca().getNombre());
        }
        if (p.getCategoria() != null) {
            dto.setNombreCategoria(p.getCategoria().getNombre());
        }
        return dto;
    }
}
