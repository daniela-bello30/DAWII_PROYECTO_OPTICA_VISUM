package pe.edu.cibertec.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagenProductoDTO {

    private Integer idImagen;

    @NotNull(message = "El ID del producto es obligatorio")
    private Integer idProducto;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String urlImagen;

    private Boolean esPrincipal;

    private Integer orden;

    private LocalDateTime fechaCreacion;
}