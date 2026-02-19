package pe.edu.cibertec.catalogo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.catalogo.config.RabbitMQConfig;
import pe.edu.cibertec.catalogo.dto.ProductoCreadoEvent;
import pe.edu.cibertec.catalogo.dto.StockActualizadoEvent;


@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;


    public void publicarProductoCreado(ProductoCreadoEvent evento) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CATALOGO_EXCHANGE,
                    RabbitMQConfig.PRODUCTO_CREADO_ROUTING_KEY,
                    evento
            );
            log.info("✅ Evento ProductoCreado publicado: {}", evento.getNombre());
        } catch (Exception e) {
            log.error("❌ Error al publicar evento ProductoCreado", e);
        }
    }


    public void publicarStockActualizado(StockActualizadoEvent evento) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CATALOGO_EXCHANGE,
                    RabbitMQConfig.STOCK_ACTUALIZADO_ROUTING_KEY,
                    evento
            );
            log.info("✅ Evento StockActualizado publicado: {} (diff: {})",
                    evento.getNombreProducto(), evento.getDiferencia());
        } catch (Exception e) {
            log.error("❌ Error al publicar evento StockActualizado", e);
        }
    }


    public void publicarProductoActualizado(Integer idProducto, String nombre) {
        try {
            // Puedes crear un evento más completo según tus necesidades
            String mensaje = String.format("Producto actualizado: %s (ID: %d)", nombre, idProducto);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CATALOGO_EXCHANGE,
                    RabbitMQConfig.PRODUCTO_ACTUALIZADO_ROUTING_KEY,
                    mensaje
            );
            log.info("✅ Evento ProductoActualizado publicado: {}", nombre);
        } catch (Exception e) {
            log.error("❌ Error al publicar evento ProductoActualizado", e);
        }
    }


    public void publicarProductoEliminado(Integer idProducto, String nombre) {
        try {
            String mensaje = String.format("Producto eliminado: %s (ID: %d)", nombre, idProducto);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CATALOGO_EXCHANGE,
                    RabbitMQConfig.PRODUCTO_ELIMINADO_ROUTING_KEY,
                    mensaje
            );
            log.info("✅ Evento ProductoEliminado publicado: {}", nombre);
        } catch (Exception e) {
            log.error("❌ Error al publicar evento ProductoEliminado", e);
        }
    }
}