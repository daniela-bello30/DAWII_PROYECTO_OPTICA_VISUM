package pe.edu.cibertec.msventas.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @Column(name = "numero_pedido", unique = true, nullable = false)
    private String numeroPedido;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_direccion_envio")
    private DireccionEnvio direccionEnvio;

    @Column(name = "tipo_entrega")
    private String tipoEntrega;

    @Column(name = "id_sucursal_recojo")
    private Integer idSucursalRecojo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "costo_envio", precision = 10, scale = 2)
    private BigDecimal costoEnvio;

    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "estado_pedido")
    private String estadoPedido;

    @Column(name = "estado_pago")
    private String estadoPago;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (fechaPedido == null) fechaPedido = LocalDateTime.now();
        if (fechaActualizacion == null) fechaActualizacion = LocalDateTime.now();
        if (numeroPedido == null) numeroPedido = "PED-" + System.currentTimeMillis();
    }

    @PreUpdate
    public void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public void calcularTotal() {
        this.subtotal = detalles.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.total = subtotal
                .add(costoEnvio != null ? costoEnvio : BigDecimal.ZERO)
                .subtract(descuento != null ? descuento : BigDecimal.ZERO);
    }

    public void confirmar() {
        this.estadoPedido = "Confirmado";
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void cancelar() {
        this.estadoPedido = "Cancelado";
        this.fechaActualizacion = LocalDateTime.now();
    }
}