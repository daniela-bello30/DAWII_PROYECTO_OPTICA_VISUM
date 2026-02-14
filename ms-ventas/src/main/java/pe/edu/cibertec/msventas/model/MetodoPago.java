package pe.edu.cibertec.msventas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "metodos_pago")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer idMetodoPago;

    @Column(name = "nombre_metodo")
    private String nombreMetodo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "icono_url")
    private String iconoUrl;

    @Column(name = "estado")
    private Boolean estado;
}