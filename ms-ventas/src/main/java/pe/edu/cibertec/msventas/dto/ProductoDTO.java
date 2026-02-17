package pe.edu.cibertec.msventas.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Integer idProducto;
    private String nombreProducto;
    private BigDecimal precioUnitario;
    private Integer stock;
    private Boolean estado;
}