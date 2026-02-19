package pe.edu.cibertec.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoSimpleDTO {

    private Integer idProducto;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private String sku;
    private String imagenPrincipal;
    private Boolean activo;

    private String nombreMarca;
    private String nombreCategoria;

    private Boolean disponible;
}