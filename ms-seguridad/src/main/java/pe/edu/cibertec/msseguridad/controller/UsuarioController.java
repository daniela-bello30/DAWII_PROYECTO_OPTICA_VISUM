package pe.edu.cibertec.msseguridad.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.msseguridad.dto.requests.ActualizarUsuarioRequest;
import pe.edu.cibertec.msseguridad.dto.response.UsuarioResponse;
import pe.edu.cibertec.msseguridad.service.interfaces.UsuarioService;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
@SecurityRequirement(name = "Bearer Authentication")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario específico por su ID. Requiere autenticación.")
    public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<UsuarioResponse>> obtenerUsuarioPorId(
            @PathVariable Long id) {
        log.info("GET /usuarios/{} - Obtener usuario por ID", id);
        UsuarioResponse usuarioResponse = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(
                pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                        usuarioResponse, "Usuario obtenido exitosamente"));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios. Requiere autenticación.")
    public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<List<UsuarioResponse>>> listarUsuarios() {
        log.info("GET /usuarios - Listar todos los usuarios");
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(
                pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                        usuarios, "Usuarios obtenidos exitosamente"));
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar usuarios activos", description = "Obtiene solo los usuarios activos. Requiere autenticación.")
    public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<List<UsuarioResponse>>> listarUsuariosActivos() {
        log.info("GET /usuarios/activos - Listar usuarios activos");
        List<UsuarioResponse> usuarios = usuarioService.listarUsuariosActivos();
        return ResponseEntity.ok(
                pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                        usuarios, "Usuarios activos obtenidos exitosamente"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente. Requiere autenticación.")
    public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<UsuarioResponse>> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request) {
        log.info("PUT /usuarios/{} - Actualizar usuario", id);
        UsuarioResponse usuarioResponse = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(
                pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                        usuarioResponse, "Usuario actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario existente. Requiere autenticación.")
    public ResponseEntity<pe.edu.cibertec.msseguridad.dto.response.ApiResponse<Void>> desactivarUsuario(
            @PathVariable Long id) {
        log.info("DELETE /usuarios/{} - Desactivar usuario", id);
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(
                pe.edu.cibertec.msseguridad.dto.response.ApiResponse.success(
                        null, "Usuario desactivado exitosamente"));
    }
}
