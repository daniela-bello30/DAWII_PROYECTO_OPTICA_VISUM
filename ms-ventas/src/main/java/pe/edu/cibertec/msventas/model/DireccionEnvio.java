package pe.edu.cibertec.msventas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "direcciones_envio")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DireccionEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer idDireccion;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre_destinatario")
    private String nombreDestinatario;

    @Column(name = "telefono_destinatario")
    private String telefonoDestinatario;

    @Column(name = "departamento")
    private String departamento;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "distrito")
    private String distrito;

    @Column(name = "direccion_linea1")
    private String direccionLinea1;

    @Column(name = "direccion_linea2")
    private String direccionLinea2;

    @Column(name = "referencia")
    private String referencia;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "es_principal")
    private Boolean esPrincipal;

    @Column(name = "estado")
    private Boolean estado;
}