package pe.edu.cibertec.msseguridad.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.msseguridad.dto.requests.ActualizarUsuarioRequest;
import pe.edu.cibertec.msseguridad.dto.response.ApiResponse;
import pe.edu.cibertec.msseguridad.dto.response.UsuarioResponse;
import pe.edu.cibertec.msseguridad.service.interfaces.UsuarioService;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> obtenerUsuarioPorId(@PathVariable Long id) {
        log.info("GET /usuarios/{} - Obtener usuario por ID", id);
        UsuarioResponse usuarioResponse = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(ApiResponse.success(usuarioResponse, "Usuario obtenido exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> listarUsuarios() {
        log.info("GET /usuarios - Listar todos los usuarios");
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(ApiResponse.success(usuarios, "Usuarios obtenidos exitosamente"));
    }

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> listarUsuariosActivos() {
        log.info("GET /usuarios/activos - Listar usuarios activos");
        List<UsuarioResponse> usuarios = usuarioService.listarUsuariosActivos();
        return ResponseEntity.ok(ApiResponse.success(usuarios, "Usuarios activos obtenidos exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request) {
        log.info("PUT /usuarios/{} - Actualizar usuario", id);
        UsuarioResponse usuarioResponse = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(ApiResponse.success(usuarioResponse, "Usuario actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desactivarUsuario(@PathVariable Long id) {
        log.info("DELETE /usuarios/{} - Desactivar usuario", id);
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Usuario desactivado exitosamente"));
    }
}
