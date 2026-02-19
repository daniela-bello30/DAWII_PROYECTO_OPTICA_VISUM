package pe.edu.cibertec.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCreadoEvent implements Serializable {

    private Integer idProducto;
    private String nombre;
    private String sku;
    private BigDecimal precio;
    private Integer stock;
    private Integer idMarca;
    private String nombreMarca;
    private Integer idCategoria;
    private String nombreCategoria;
    private LocalDateTime fechaCreacion;
    private String creadoPor;
}