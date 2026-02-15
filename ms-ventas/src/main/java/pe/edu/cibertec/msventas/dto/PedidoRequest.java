package pe.edu.cibertec.msventas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Integer idUsuario;

    private Integer idDireccionEnvio;

    @NotNull(message = "El tipo de entrega es obligatorio")
    private String tipoEntrega;

    private Integer idSucursalRecojo;

    @NotNull(message = "El método de pago es obligatorio")
    private Integer idMetodoPago;

    @NotNull(message = "Los detalles del pedido son obligatorios")
    private List<DetallePedidoRequest> detalles;

    private String notas;

    @Data
    public static class DetallePedidoRequest {
        @NotNull
        private Integer idProducto;

        @NotNull
        private Integer cantidad;
    }
}