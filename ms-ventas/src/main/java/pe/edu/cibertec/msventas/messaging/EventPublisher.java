package pe.edu.cibertec.msventas.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.msventas.model.Pedido;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "optica.exchange";
    private static final String ROUTING_KEY = "optica.ventas";

    public void publicarPedidoCreado(Pedido pedido) {
        publicarEvento("PedidoCreadoEvent", pedido);
    }

    public void publicarPedidoConfirmado(Pedido pedido) {
        publicarEvento("PedidoConfirmadoEvent", pedido);
    }

    public void publicarPedidoCancelado(Pedido pedido) {
        publicarEvento("PedidoCanceladoEvent", pedido);
    }

    private void publicarEvento(String tipoEvento, Pedido pedido) {
        try {
            Map<String, Object> evento = buildEvento(tipoEvento, pedido);
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, evento);

            log.info("📤 Evento publicado: {} - Pedido: {}", tipoEvento, pedido.getNumeroPedido());
        } catch (Exception e) {
            log.error("❌ Error al publicar evento {}: {}", tipoEvento, e.getMessage());
        }
    }

    private Map<String, Object> buildEvento(String tipoEvento, Pedido pedido) {
        Map<String, Object> evento = new HashMap<>();
        evento.put("tipoEvento", tipoEvento);
        evento.put("numeroPedido", pedido.getNumeroPedido());
        evento.put("idPedido", pedido.getIdPedido());
        evento.put("idUsuario", pedido.getIdUsuario());
        evento.put("total", pedido.getTotal());
        evento.put("estadoPedido", pedido.getEstadoPedido());
        evento.put("estadoPago", pedido.getEstadoPago());
        evento.put("fechaPedido", pedido.getFechaPedido() != null
                ? pedido.getFechaPedido().toString()
                : null);

        if (pedido.getDireccionEnvio() != null) {
            Map<String, Object> direccion = new HashMap<>();
            direccion.put("nombreDestinatario", pedido.getDireccionEnvio().getNombreDestinatario());
            direccion.put("direccion", pedido.getDireccionEnvio().getDireccionLinea1());
            direccion.put("distrito", pedido.getDireccionEnvio().getDistrito());
            direccion.put("telefono", pedido.getDireccionEnvio().getTelefonoDestinatario());
            evento.put("direccionEnvio", direccion);
        }

        if (pedido.getMetodoPago() != null) {
            evento.put("metodoPago", pedido.getMetodoPago().getNombreMetodo());
        }

        int cantidadTotal = pedido.getDetalles().stream()
                .mapToInt(d -> d.getCantidad())
                .sum();
        evento.put("cantidadProductos", cantidadTotal);

        return evento;
    }
}