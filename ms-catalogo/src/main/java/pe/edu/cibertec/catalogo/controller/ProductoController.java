package pe.edu.cibertec.catalogo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.cibertec.catalogo.dto.*;
import pe.edu.cibertec.catalogo.service.ProductoService;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión completa de productos del catálogo")

public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> obtenerTodos() {
        List<ProductoDTO> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(
                ApiResponseDTO.success("Productos obtenidos exitosamente", productos)
        );
    }


    @GetMapping("/paginado")
    @Operation(summary = "Listar productos con paginación")
    public ResponseEntity<ApiResponseDTO<Page<ProductoDTO>>> obtenerTodosPaginado(
            @Parameter(description = "Número de página (inicia en 0)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Cantidad de items por página")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo para ordenar")
            @RequestParam(defaultValue = "nombre") String sortBy) {

        Page<ProductoDTO> productos = productoService.obtenerTodosPaginado(page, size, sortBy);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Productos paginados obtenidos", productos)
        );
    }


    @GetMapping("/activos")
    @Operation(summary = "Listar productos activos", description = "Obtiene solo productos disponibles")
    public ResponseEntity<ApiResponseDTO<List<ProductoSimpleDTO>>> obtenerActivos() {
        List<ProductoSimpleDTO> productos = productoService.obtenerActivos();
        return ResponseEntity.ok(
                ApiResponseDTO.success("Productos activos obtenidos", productos)
        );
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> obtenerPorId(@PathVariable Integer id) {
        ProductoDTO producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Producto encontrado", producto)
        );
    }


    @GetMapping("/sku/{sku}")
    @Operation(summary = "Obtener producto por SKU")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> obtenerPorSku(@PathVariable String sku) {
        ProductoDTO producto = productoService.obtenerPorSku(sku);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Producto encontrado", producto)
        );
    }


    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos por nombre")
    public ResponseEntity<ApiResponseDTO<List<ProductoSimpleDTO>>> buscarPorNombre(
            @RequestParam String nombre) {
        List<ProductoSimpleDTO> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Búsqueda completada", productos)
        );
    }


    @GetMapping("/busqueda-avanzada")
    @Operation(summary = "Búsqueda avanzada",
            description = "Busca en nombre, marca y categoría simultáneamente")
    public ResponseEntity<ApiResponseDTO<Page<ProductoDTO>>> busquedaAvanzada(
            @RequestParam(name = "q") String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductoDTO> productos = productoService.busquedaAvanzada(searchTerm, page, size);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Búsqueda avanzada completada", productos)
        );
    }


    @GetMapping("/marca/{idMarca}")
    @Operation(summary = "Filtrar por marca")
    public ResponseEntity<ApiResponseDTO<List<ProductoSimpleDTO>>> filtrarPorMarca(
            @PathVariable Integer idMarca) {
        List<ProductoSimpleDTO> productos = productoService.filtrarPorMarca(idMarca);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Productos filtrados por marca", productos)
        );
    }


    @GetMapping("/categoria/{idCategoria}")
    @Operation(summary = "Filtrar por categoría")
    public ResponseEntity<ApiResponseDTO<List<ProductoSimpleDTO>>> filtrarPorCategoria(
            @PathVariable Integer idCategoria) {
        List<ProductoSimpleDTO> productos = productoService.filtrarPorCategoria(idCategoria);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Productos filtrados por categoría", productos)
        );
    }

    @GetMapping("/filtrar")
    @Operation(summary = "Filtrado múltiple",
            description = "Filtra por marca, categoría y rango de precio")
    public ResponseEntity<ApiResponseDTO<Page<ProductoDTO>>> filtrar(
            @RequestParam(required = false) Integer marca,
            @RequestParam(required = false) Integer categoria,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductoDTO> productos = productoService.filtrarProductos(
                marca, categoria, precioMin, precioMax, page, size
        );
        return ResponseEntity.ok(
                ApiResponseDTO.success("Filtrado completado", productos)
        );
    }

    @GetMapping("/con-stock")
    @Operation(summary = "Productos con stock disponible")
    public ResponseEntity<ApiResponseDTO<List<ProductoSimpleDTO>>> obtenerConStock() {
        List<ProductoSimpleDTO> productos = productoService.obtenerProductosConStock();
        return ResponseEntity.ok(
                ApiResponseDTO.success("Productos con stock obtenidos", productos)
        );
    }

    @GetMapping("/populares")
    @Operation(summary = "Productos populares")
    public ResponseEntity<ApiResponseDTO<Page<ProductoDTO>>> obtenerPopulares(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductoDTO> productos = productoService.obtenerProductosPopulares(page, size);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Productos populares obtenidos", productos)
        );
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> crear(
            @Valid @RequestBody ProductoRequestDTO productoRequest) {
        ProductoDTO nuevoProducto = productoService.crear(productoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.success("Producto creado exitosamente", nuevoProducto)
        );
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoRequestDTO productoRequest) {
        ProductoDTO productoActualizado = productoService.actualizar(id, productoRequest);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Producto actualizado exitosamente", productoActualizado)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Producto eliminado exitosamente")
        );
    }

    @PatchMapping("/{id}/reactivar")
    @Operation(summary = "Reactivar producto")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> reactivar(@PathVariable Integer id) {
        ProductoDTO producto = productoService.reactivar(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Producto reactivado exitosamente", producto)
        );
    }

    @GetMapping("/{id}/verificar-stock")
    @Operation(summary = "Verificar stock disponible",
            description = "Endpoint consumido por ms-ventas mediante OpenFeign")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarStock(
            @PathVariable Integer id,
            @RequestParam Integer cantidad) {
        boolean hayStock = productoService.verificarStock(id, cantidad);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Verificación de stock completada", hayStock)
        );
    }

    @PatchMapping("/{id}/reducir-stock")
    @Operation(summary = "Reducir stock",
            description = "Llamado por ms-ventas cuando se confirma una venta")
    public ResponseEntity<ApiResponseDTO<Void>> reducirStock(
            @PathVariable Integer id,
            @RequestParam Integer cantidad) {
        productoService.reducirStock(id, cantidad);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Stock reducido exitosamente")
        );
    }

    @PatchMapping("/{id}/aumentar-stock")
    @Operation(summary = "Aumentar stock",
            description = "Llamado en devoluciones o reabastecimiento")
    public ResponseEntity<ApiResponseDTO<Void>> aumentarStock(
            @PathVariable Integer id,
            @RequestParam Integer cantidad) {
        productoService.aumentarStock(id, cantidad);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Stock aumentado exitosamente")
        );
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock",
            description = "Establece un nuevo valor de stock")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> actualizarStock(
            @PathVariable Integer id,
            @RequestParam Integer nuevoStock) {
        ProductoDTO producto = productoService.actualizarStock(id, nuevoStock);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Stock actualizado exitosamente", producto)
        );
    }
}