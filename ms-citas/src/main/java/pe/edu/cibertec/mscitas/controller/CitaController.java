package pe.edu.cibertec.mscitas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.mscitas.dto.CitaDTO;
import pe.edu.cibertec.mscitas.service.CitaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Tag(name = "Citas", description = "API para gestión de citas")
public class CitaController {

    private final CitaService citaService;

    @Operation(summary = "Obtener todas las citas")
    @GetMapping
    public ResponseEntity<List<CitaDTO>> obtenerTodas() {
        List<CitaDTO> citas = citaService.obtenerTodas();
        return ResponseEntity.ok(citas);
    }

    @Operation(summary = "Obtener cita por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> obtenerPorId(@PathVariable Integer id) {
        CitaDTO cita = citaService.obtenerPorId(id);
        return ResponseEntity.ok(cita);
    }

    @Operation(summary = "Obtener citas por usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CitaDTO>> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        List<CitaDTO> citas = citaService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(citas);
    }

    @Operation(summary = "Crear nueva cita",
            description = "Crea una nueva cita y envía un evento a RabbitMQ para notificaciones")
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody CitaDTO citaDTO) {
        try {
            CitaDTO nuevaCita = citaService.crear(citaDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cita creada exitosamente. Se ha enviado una notificación.");
            response.put("data", nuevaCita);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Actualizar cita existente")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CitaDTO citaDTO) {
        try {
            CitaDTO citaActualizada = citaService.actualizar(id, citaDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cita actualizada exitosamente");
            response.put("data", citaActualizada);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Cancelar cita")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> cancelar(@PathVariable Integer id) {
        try {
            citaService.cancelar(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cita cancelada exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}