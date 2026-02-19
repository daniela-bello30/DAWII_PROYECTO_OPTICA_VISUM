package pe.edu.cibertec.catalogo.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoEvent {

    private ProductoEventType tipo;
    private Long productoId;
    private String nombre;
    private BigDecimal precio;
    private LocalDateTime fecha;

    public ProductoEvent(ProductoEventType tipo,
                         Long productoId,
                         String nombre,
                         BigDecimal precio,
                         LocalDateTime fecha){
        this.tipo = tipo;
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.fecha = fecha;
    }

    public ProductoEventType getTipo() {
        return tipo;
    }

    public void setTipo(ProductoEventType tipo) {
        this.tipo = tipo;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
