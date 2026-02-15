package pe.edu.cibertec.mscitas.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres de componentes RabbitMQ
    public static final String EXCHANGE_NAME = "optica.exchange";
    public static final String QUEUE_CITAS = "optica.citas.queue";
    public static final String ROUTING_KEY_CITAS = "optica.citas";

    /**
     * Exchange tipo Topic para enrutar mensajes
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * Cola para eventos de citas
     */
    @Bean
    public Queue citasQueue() {
        return new Queue(QUEUE_CITAS, true); // durable = true
    }

    /**
     * Binding entre el exchange y la cola
     */
    @Bean
    public Binding binding(Queue citasQueue, TopicExchange exchange) {
        return BindingBuilder
                .bind(citasQueue)
                .to(exchange)
                .with(ROUTING_KEY_CITAS + ".#");
    }

    /**
     * Convertidor de mensajes a JSON
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitTemplate configurado con el convertidor JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}