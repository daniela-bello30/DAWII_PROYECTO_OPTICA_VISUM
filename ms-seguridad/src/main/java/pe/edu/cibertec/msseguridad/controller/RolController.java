package pe.edu.cibertec.msseguridad.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.msseguridad.dto.requests.CrearRolRequest;

import pe.edu.cibertec.msseguridad.dto.response.RolResponse;
import pe.edu.cibertec.msseguridad.service.interfaces.RolService;

import java.util.List;

@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Roles", description = "Endpoints para gestión de roles (Solo ADMIN)")
@SecurityRequirement(name = "Bearer Authentication")
public class RolController {

    private final RolService rolService;

    @PostMapping
        @Operation(summary = "Crear rol", description = "Crea un nuevo rol en el sistema. Requiere rol ADMIN.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Rol creado exitosamente"),
                @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                @ApiResponse(responseCode = "403", description = "No tiene permisos (requiere rol ADMIN)")
        })
        public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<RolResponse>> crearRol(
                @Valid @RequestBody CrearRolRequest request) {
            log.info("POST /roles - Crear nuevo rol");
            RolResponse rolResponse = rolService.crearRol(request);
            return new ResponseEntity<>(
                    pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                            rolResponse, "Rol creado exitosamente"),
                    HttpStatus.CREATED
            );
        }

        @GetMapping
            @Operation(summary = "Listar roles", description = "Obtiene todos los roles. Requiere rol ADMIN.")
            public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<List<RolResponse>>> listarRoles() {
                log.info("GET /roles - Listar todos los roles");
                List<RolResponse> roles = rolService.listarRoles();
                return ResponseEntity.ok(
                        pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                                roles, "Roles obtenidos exitosamente"));
            }

            @GetMapping("/activos")
                @Operation(summary = "Listar roles activos", description = "Obtiene solo los roles activos. Requiere rol ADMIN.")
                public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<List<RolResponse>>> listarRolesActivos() {
                    log.info("GET /roles/activos - Listar roles activos");
                    List<RolResponse> roles = rolService.listarRolesActivos();
                    return ResponseEntity.ok(
                            pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                                    roles, "Roles activos obtenidos exitosamente"));
                }

                @GetMapping("/{id}")
                    @Operation(summary = "Obtener rol por ID", description = "Obtiene un rol específico por su ID. Requiere rol ADMIN.")
                    public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<RolResponse>> obtenerRolPorId(
                            @PathVariable Long id) {
                        log.info("GET /roles/{} - Obtener rol por ID", id);
                        RolResponse rolResponse = rolService.obtenerRolPorId(id);
                        return ResponseEntity.ok(
                                pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                                        rolResponse, "Rol obtenido exitosamente"));
                    }

                    @PutMapping("/{id}")
                    @Operation(summary = "Actualizar rol", description = "Actualiza un rol existente. Requiere rol ADMIN.")
                    public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<RolResponse>> actualizarRol(
                            @PathVariable Long id,
                            @Valid @RequestBody CrearRolRequest request) {
                        log.info("PUT /roles/{} - Actualizar rol", id);
                        RolResponse rolResponse = rolService.actualizarRol(id, request);
                        return ResponseEntity.ok(
                                pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                                        rolResponse, "Rol actualizado exitosamente"));
                    }

                    @DeleteMapping("/{id}")

                        @Operation(summary = "Desactivar rol", description = "Desactiva un rol existente. Requiere rol ADMIN.")
                        public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<Void>> desactivarRol(
                                @PathVariable Long id) {
                            log.info("DELETE /roles/{} - Desactivar rol", id);
                            rolService.desactivarRol(id);
                            return ResponseEntity.ok(
                                    pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                                            null, "Rol desactivado exitosamente"));
                        }
                }