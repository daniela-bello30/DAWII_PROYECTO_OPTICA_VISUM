package pe.edu.cibertec.catalogo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.cibertec.catalogo.dto.ApiResponseDTO;
import pe.edu.cibertec.catalogo.dto.ImagenProductoDTO;
import pe.edu.cibertec.catalogo.service.ImagenProductoService;

import java.util.List;


@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
@Tag(name = "Imágenes", description = "Gestión de imágenes de productos")

public class ImagenProductoController {

    private final ImagenProductoService imagenProductoService;

    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Obtener imágenes de un producto")
    public ResponseEntity<ApiResponseDTO<List<ImagenProductoDTO>>> obtenerImagenesDeProducto(
            @PathVariable Integer idProducto) {
        List<ImagenProductoDTO> imagenes = imagenProductoService.obtenerImagenesDeProducto(idProducto);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Imágenes obtenidas exitosamente", imagenes)
        );
    }


    @GetMapping("/producto/{idProducto}/principal")
    @Operation(summary = "Obtener imagen principal de un producto")
    public ResponseEntity<ApiResponseDTO<ImagenProductoDTO>> obtenerImagenPrincipal(
            @PathVariable Integer idProducto) {
        ImagenProductoDTO imagen = imagenProductoService.obtenerImagenPrincipal(idProducto);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Imagen principal obtenida", imagen)
        );
    }

    @PostMapping
    @Operation(summary = "Agregar nueva imagen")
    public ResponseEntity<ApiResponseDTO<ImagenProductoDTO>> agregarImagen(
            @Valid @RequestBody ImagenProductoDTO imagenDTO) {
        ImagenProductoDTO nuevaImagen = imagenProductoService.agregarImagen(imagenDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO.success("Imagen agregada exitosamente", nuevaImagen)
        );
    }


    @PatchMapping("/{id}/establecer-principal")
    @Operation(summary = "Establecer como imagen principal")
    public ResponseEntity<ApiResponseDTO<ImagenProductoDTO>> establecerComoPrincipal(
            @PathVariable Integer id) {
        ImagenProductoDTO imagen = imagenProductoService.establecerComoPrincipal(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Imagen establecida como principal", imagen)
        );
    }


    @PatchMapping("/{id}/orden")
    @Operation(summary = "Actualizar orden de imagen")
    public ResponseEntity<ApiResponseDTO<ImagenProductoDTO>> actualizarOrden(
            @PathVariable Integer id,
            @RequestParam Integer nuevoOrden) {
        ImagenProductoDTO imagen = imagenProductoService.actualizarOrden(id, nuevoOrden);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Orden actualizado exitosamente", imagen)
        );
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar imagen")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarImagen(@PathVariable Integer id) {
        imagenProductoService.eliminarImagen(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Imagen eliminada exitosamente")
        );
    }


    @DeleteMapping("/producto/{idProducto}")
    @Operation(summary = "Eliminar todas las imágenes de un producto")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarTodasLasImagenes(
            @PathVariable Integer idProducto) {
        imagenProductoService.eliminarTodasLasImagenesDeProducto(idProducto);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Todas las imágenes del producto fueron eliminadas")
        );
    }
}