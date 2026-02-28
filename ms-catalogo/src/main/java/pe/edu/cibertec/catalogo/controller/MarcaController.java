package pe.edu.cibertec.catalogo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.catalogo.dto.ApiResponseDTO;
import pe.edu.cibertec.catalogo.dto.MarcaDTO;
import pe.edu.cibertec.catalogo.service.MarcaService;

import java.util.List;


@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
@Tag(name = "Marcas", description = "Gestión de marcas de productos")

public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping
    @Operation(summary = "Listar todas las marcas", description = "Obtiene un listado de todas las marcas registradas")
    public ResponseEntity<ApiResponseDTO<List<MarcaDTO>>> obtenerTodas() {
        List<MarcaDTO> marcas = marcaService.obtenerTodas();
        return ResponseEntity.ok(
                ApiResponseDTO.success("Marcas obtenidas exitosamente", marcas)
        );
    }


    @GetMapping("/activas")
    @Operation(summary = "Listar marcas activas", description = "Obtiene solo las marcas que están activas")
    public ResponseEntity<ApiResponseDTO<List<MarcaDTO>>> obtenerActivas() {
        List<MarcaDTO> marcas = marcaService.obtenerActivas();
        return ResponseEntity.ok(
                ApiResponseDTO.success("Marcas activas obtenidas exitosamente", marcas)
        );
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener marca por ID", description = "Busca una marca específica por su ID")
    public ResponseEntity<ApiResponseDTO<MarcaDTO>> obtenerPorId(@PathVariable Integer id) {
        MarcaDTO marca = marcaService.obtenerPorId(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Marca encontrada", marca)
        );
    }


    @GetMapping("/buscar")
    @Operation(summary = "Buscar marcas por nombre", description = "Realiza una búsqueda parcial por nombre")
    public ResponseEntity<ApiResponseDTO<List<MarcaDTO>>> buscarPorNombre(
            @RequestParam String nombre) {
        List<MarcaDTO> marcas = marcaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Búsqueda completada", marcas)
        );
    }


    @GetMapping("/pais/{pais}")
    @Operation(summary = "Buscar marcas por país", description = "Filtra marcas por país de origen")
    public ResponseEntity<ApiResponseDTO<List<MarcaDTO>>> buscarPorPais(@PathVariable String pais) {
        List<MarcaDTO> marcas = marcaService.buscarPorPais(pais);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Marcas del país " + pais, marcas)
        );
    }


    @PostMapping
    @Operation(summary = "Crear nueva marca", description = "Registra una nueva marca en el sistema")
    public ResponseEntity<ApiResponseDTO<MarcaDTO>> crear(@Valid @RequestBody MarcaDTO marcaDTO) {
        MarcaDTO nuevaMarca = marcaService.crear(marcaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.success("Marca creada exitosamente", nuevaMarca)
        );
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar marca", description = "Modifica los datos de una marca existente")
    public ResponseEntity<ApiResponseDTO<MarcaDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody MarcaDTO marcaDTO) {
        MarcaDTO marcaActualizada = marcaService.actualizar(id, marcaDTO);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Marca actualizada exitosamente", marcaActualizada)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar marca", description = "Desactiva una marca (no la elimina permanentemente)")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Integer id) {
        marcaService.eliminar(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Marca eliminada exitosamente")
        );
    }


    @PatchMapping("/{id}/reactivar")
    @Operation(summary = "Reactivar marca", description = "Reactiva una marca que fue desactivada")
    public ResponseEntity<ApiResponseDTO<MarcaDTO>> reactivar(@PathVariable Integer id) {
        MarcaDTO marca = marcaService.reactivar(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Marca reactivada exitosamente", marca)
        );
    }
}