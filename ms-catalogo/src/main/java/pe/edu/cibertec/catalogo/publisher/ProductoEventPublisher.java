package pe.edu.cibertec.catalogo.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.catalogo.config.RabbitMQConfig;
import pe.edu.cibertec.catalogo.events.ProductoEvent;

@Component
@RequiredArgsConstructor
public class ProductoEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(ProductoEvent evento){

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                evento
        );
    }
}
