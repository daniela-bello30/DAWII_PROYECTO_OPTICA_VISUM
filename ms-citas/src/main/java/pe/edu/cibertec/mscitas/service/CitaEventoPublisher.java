package pe.edu.cibertec.mscitas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.mscitas.config.RabbitMQConfig;
import pe.edu.cibertec.mscitas.dto.CitaEventoDTO;

/**
 * Servicio optimizado para publicar eventos de citas en RabbitMQ con enrutamiento dinámico.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CitaEventoPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publica un evento de cita en el Exchange usando una routing key específica por tipo de evento.
     * Ejemplo de routing key generada: "optica.citas.creada", "optica.citas.cancelada".
     */
    public void publicarEvento(CitaEventoDTO evento) {
        try {
            // 1. Generamos una routing key dinámica basada en el tipo de evento (creada, actualizada, cancelada)
            // Esto permite que los consumidores (como ms-notificaciones) filtren mejor los mensajes.
            String routingKey = RabbitMQConfig.ROUTING_KEY_CITAS + "." + evento.getTipoEvento().toLowerCase();

            log.info("📤 Publicando evento en RabbitMQ -> Exchange: '{}', RoutingKey: '{}', Cita Nro: '{}'",
                    RabbitMQConfig.EXCHANGE_NAME, routingKey, evento.getNumeroCita());

            // 2. Enviamos el objeto al Exchange
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    routingKey,
                    evento
            );

            log.info("✅ Evento enviado exitosamente para la sucursal: {}", evento.getNombreSucursal());

        } catch (Exception e) {
            log.error("❌ ERROR CRÍTICO al publicar evento en RabbitMQ: {}", e.getMessage(), e);
            // Aquí podrías implementar una lógica de reintento o guardar en una tabla de fallos si fuera necesario.
        }
    }
}