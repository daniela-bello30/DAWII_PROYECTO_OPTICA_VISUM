package pe.edu.cibertec.catalogo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Constantes principales
    public static final String EXCHANGE = "catalogo.exchange";
    public static final String QUEUE = "catalogo.productos.queue";
    public static final String ROUTING_KEY = "producto.event";

    // Alias y constantes extendidas usadas por EventPublisherService
    public static final String CATALOGO_EXCHANGE = EXCHANGE;
    public static final String PRODUCTO_CREADO_ROUTING_KEY = "producto.creado";
    public static final String STOCK_ACTUALIZADO_ROUTING_KEY = "stock.actualizado";
    public static final String PRODUCTO_ACTUALIZADO_ROUTING_KEY = "producto.actualizado";
    public static final String PRODUCTO_ELIMINADO_ROUTING_KEY = "producto.eliminado";

    // ======================
    // EXCHANGE
    // ======================
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    // ======================
    // QUEUE
    // ======================
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    // ======================
    // BINDING
    // ======================
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    // ======================
    // JSON CONVERTER ⭐
    // ======================
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ======================
    // RABBIT TEMPLATE
    // ======================
    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}