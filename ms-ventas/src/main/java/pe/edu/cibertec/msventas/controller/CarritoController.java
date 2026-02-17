package pe.edu.cibertec.msventas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.msventas.dto.CarritoRequest;
import pe.edu.cibertec.msventas.model.Carrito;
import pe.edu.cibertec.msventas.service.CarritoService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
@Tag(name = "Carrito", description = "API para gestión de carrito de compras")
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Obtener carrito de un usuario")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(carritoService.obtenerCarritoPorUsuario(idUsuario));
    }

    @PostMapping("/agregar")
    @Operation(summary = "Agregar item al carrito",
            description = "Verifica stock en ms-catalogo antes de agregar")
    public ResponseEntity<Map<String, Object>> agregarItem(@Valid @RequestBody CarritoRequest request) {

        Carrito carrito = carritoService.agregarItemAlCarrito(
                request.getIdUsuario(),
                request.getIdProducto(),
                request.getCantidad()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Producto agregado al carrito");
        response.put("data", carrito);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/usuario/{idUsuario}/item/{idDetalle}")
    @Operation(summary = "Actualizar cantidad de un item")
    public ResponseEntity<Carrito> actualizarCantidad(
            @PathVariable Integer idUsuario,
            @PathVariable Integer idDetalle,
            @RequestParam Integer nuevaCantidad) {

        Carrito carrito = carritoService.actualizarCantidadItem(idUsuario, idDetalle, nuevaCantidad);
        return ResponseEntity.ok(carrito);
    }

    @DeleteMapping("/usuario/{idUsuario}/item/{idDetalle}")
    @Operation(summary = "Eliminar item del carrito")
    public ResponseEntity<Map<String, Object>> eliminarItem(
            @PathVariable Integer idUsuario,
            @PathVariable Integer idDetalle) {

        carritoService.eliminarItemDelCarrito(idUsuario, idDetalle);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Item eliminado del carrito");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/usuario/{idUsuario}")
    @Operation(summary = "Vaciar carrito")
    public ResponseEntity<Map<String, Object>> vaciarCarrito(@PathVariable Integer idUsuario) {
        carritoService.vaciarCarrito(idUsuario);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Carrito vaciado");

        return ResponseEntity.ok(response);
    }
}