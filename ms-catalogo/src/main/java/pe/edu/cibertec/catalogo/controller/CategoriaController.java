package pe.edu.cibertec.catalogo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.cibertec.catalogo.dto.ApiResponseDTO;
import pe.edu.cibertec.catalogo.dto.CategoriaDTO;
import pe.edu.cibertec.catalogo.service.CategoriaService;

import java.util.List;


@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;


    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<ApiResponseDTO<List<CategoriaDTO>>> obtenerTodas() {
        List<CategoriaDTO> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(
                ApiResponseDTO.success("Categorías obtenidas exitosamente", categorias)
        );
    }


    @GetMapping("/activas")
    @Operation(summary = "Listar categorías activas")
    public ResponseEntity<ApiResponseDTO<List<CategoriaDTO>>> obtenerActivas() {
        List<CategoriaDTO> categorias = categoriaService.obtenerActivas();
        return ResponseEntity.ok(
                ApiResponseDTO.success("Categorías activas obtenidas", categorias)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<ApiResponseDTO<CategoriaDTO>> obtenerPorId(@PathVariable Integer id) {
        CategoriaDTO categoria = categoriaService.obtenerPorId(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Categoría encontrada", categoria)
        );
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar categorías por nombre")
    public ResponseEntity<ApiResponseDTO<List<CategoriaDTO>>> buscarPorNombre(
            @RequestParam String nombre) {
        List<CategoriaDTO> categorias = categoriaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Búsqueda completada", categorias)
        );
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría")
    public ResponseEntity<ApiResponseDTO<CategoriaDTO>> crear(
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO nuevaCategoria = categoriaService.crear(categoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.success("Categoría creada exitosamente", nuevaCategoria)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría")
    public ResponseEntity<ApiResponseDTO<CategoriaDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        CategoriaDTO categoriaActualizada = categoriaService.actualizar(id, categoriaDTO);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Categoría actualizada exitosamente", categoriaActualizada)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Categoría eliminada exitosamente")
        );
    }

    @PatchMapping("/{id}/reactivar")
    @Operation(summary = "Reactivar categoría")
    public ResponseEntity<ApiResponseDTO<CategoriaDTO>> reactivar(@PathVariable Integer id) {
        CategoriaDTO categoria = categoriaService.reactivar(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Categoría reactivada exitosamente", categoria)
        );
    }
}