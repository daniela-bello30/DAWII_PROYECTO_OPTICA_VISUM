package pe.edu.cibertec.msseguridad.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.msseguridad.dto.requests.CrearRolRequest;
import pe.edu.cibertec.msseguridad.dto.response.ApiResponse;
import pe.edu.cibertec.msseguridad.dto.response.RolResponse;
import pe.edu.cibertec.msseguridad.service.interfaces.RolService;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RolController {

    private final RolService rolService;

    @PostMapping
    public ResponseEntity<ApiResponse<RolResponse>> crearRol(@Valid @RequestBody CrearRolRequest request) {
        log.info("POST /roles - Crear nuevo rol");
        RolResponse rolResponse = rolService.crearRol(request);
        return new ResponseEntity<>(
                ApiResponse.success(rolResponse, "Rol creado exitosamente"),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RolResponse>>> listarRoles() {
        log.info("GET /roles - Listar todos los roles");
        List<RolResponse> roles = rolService.listarRoles();
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles obtenidos exitosamente"));
    }

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<RolResponse>>> listarRolesActivos() {
        log.info("GET /roles/activos - Listar roles activos");
        List<RolResponse> roles = rolService.listarRolesActivos();
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles activos obtenidos exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RolResponse>> obtenerRolPorId(@PathVariable Long id) {
        log.info("GET /roles/{} - Obtener rol por ID", id);
        RolResponse rolResponse = rolService.obtenerRolPorId(id);
        return ResponseEntity.ok(ApiResponse.success(rolResponse, "Rol obtenido exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RolResponse>> actualizarRol(
            @PathVariable Long id,
            @Valid @RequestBody CrearRolRequest request) {
        log.info("PUT /roles/{} - Actualizar rol", id);
        RolResponse rolResponse = rolService.actualizarRol(id, request);
        return ResponseEntity.ok(ApiResponse.success(rolResponse, "Rol actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desactivarRol(@PathVariable Long id) {
        log.info("DELETE /roles/{} - Desactivar rol", id);
        rolService.desactivarRol(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Rol desactivado exitosamente"));
    }
}
