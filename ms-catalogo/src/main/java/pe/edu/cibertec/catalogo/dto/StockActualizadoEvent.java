package pe.edu.cibertec.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockActualizadoEvent implements Serializable {

    private Integer idProducto;
    private String sku;
    private String nombreProducto;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private Integer diferencia;
    private String motivo;
    private LocalDateTime fechaActualizacion;
}