package pe.edu.cibertec.mscitas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.mscitas.model.Sucursal;
import pe.edu.cibertec.mscitas.service.SucursalService;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursales", description = "API para gestión de sucursales")
public class SucursalController {

    private final SucursalService sucursalService;

    @Operation(summary = "Obtener todas las sucursales")
    @GetMapping
    public ResponseEntity<List<Sucursal>> obtenerTodas() {
        List<Sucursal> sucursales = sucursalService.obtenerTodas();
        return ResponseEntity.ok(sucursales);
    }

    @Operation(summary = "Obtener sucursales activas")
    @GetMapping("/activas")
    public ResponseEntity<List<Sucursal>> obtenerActivas() {
        List<Sucursal> sucursales = sucursalService.obtenerActivas();
        return ResponseEntity.ok(sucursales);
    }

    @Operation(summary = "Obtener sucursal por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerPorId(@PathVariable Integer id) {
        Sucursal sucursal = sucursalService.obtenerPorId(id);
        return ResponseEntity.ok(sucursal);
    }
}