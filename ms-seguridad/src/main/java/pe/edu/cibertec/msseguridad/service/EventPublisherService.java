package pe.edu.cibertec.msseguridad.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.msseguridad.config.RabbitMQConfig;
import pe.edu.cibertec.msseguridad.dto.events.UsuarioActualizadoEvent;
import pe.edu.cibertec.msseguridad.dto.events.UsuarioRegistradoEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void publicarUsuarioRegistrado(UsuarioRegistradoEvent evento) {
        try {
            log.info("Publicando evento de usuario registrado: {}", evento.getEmail());
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USUARIO_EXCHANGE,
                    RabbitMQConfig.USUARIO_REGISTRADO_ROUTING_KEY,
                    evento
            );
            log.info("Evento publicado exitosamente");
        } catch (Exception e) {
            log.error("Error al publicar evento de usuario registrado", e);
        }
    }

    public void publicarUsuarioActualizado(UsuarioActualizadoEvent evento) {
        try {
            log.info("Publicando evento de usuario actualizado: {}", evento.getEmail());
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USUARIO_EXCHANGE,
                    RabbitMQConfig.USUARIO_ACTUALIZADO_ROUTING_KEY,
                    evento
            );
            log.info("Evento publicado exitosamente");
        } catch (Exception e) {
            log.error("Error al publicar evento de usuario actualizado", e);
        }
    }
}
