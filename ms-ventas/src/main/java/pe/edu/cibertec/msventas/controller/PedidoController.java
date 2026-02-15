package pe.edu.cibertec.msventas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.msventas.dto.PedidoRequest;
import pe.edu.cibertec.msventas.model.DetallePedido;
import pe.edu.cibertec.msventas.model.DireccionEnvio;
import pe.edu.cibertec.msventas.model.MetodoPago;
import pe.edu.cibertec.msventas.model.Pedido;
import pe.edu.cibertec.msventas.service.PedidoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API para gestión de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear nuevo pedido",
            description = "Crea un pedido, verifica stock en ms-catalogo y publica evento")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody PedidoRequest request) {

        // Construir pedido desde DTO
        Pedido pedido = Pedido.builder()
                .idUsuario(request.getIdUsuario())
                .direccionEnvio(request.getIdDireccionEnvio() != null
                        ? DireccionEnvio.builder().idDireccion(request.getIdDireccionEnvio()).build()
                        : null)
                .tipoEntrega(request.getTipoEntrega())
                .idSucursalRecojo(request.getIdSucursalRecojo())
                .metodoPago(MetodoPago.builder().idMetodoPago(request.getIdMetodoPago()).build())
                .notas(request.getNotas())
                .detalles(request.getDetalles().stream()
                        .map(d -> DetallePedido.builder()
                                .idProducto(d.getIdProducto())
                                .cantidad(d.getCantidad())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        Pedido creado = pedidoService.crearPedido(pedido);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Pedido creado exitosamente");
        response.put("data", creado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<Pedido> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Obtener pedidos de un usuario")
    public ResponseEntity<List<Pedido>> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorUsuario(idUsuario));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los pedidos")
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodosPedidos());
    }

    @PutMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar pedido",
            description = "Confirma el pedido y reduce stock en ms-catalogo")
    public ResponseEntity<Map<String, Object>> confirmar(@PathVariable Integer id) {
        Pedido confirmado = pedidoService.confirmarPedido(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Pedido confirmado y stock reducido");
        response.put("data", confirmado);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido",
            description = "Cancela el pedido y restaura stock si fue confirmado")
    public ResponseEntity<Map<String, Object>> cancelar(@PathVariable Integer id) {
        pedidoService.cancelarPedido(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Pedido cancelado exitosamente");

        return ResponseEntity.ok(response);
    }
}