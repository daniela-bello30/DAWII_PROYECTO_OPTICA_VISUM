package pe.edu.cibertec.msseguridad.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres de exchanges
    public static final String USUARIO_EXCHANGE = "usuario.exchange";

    // Nombres de colas
    public static final String USUARIO_REGISTRADO_QUEUE = "usuario.registrado.queue";
    public static final String USUARIO_ACTUALIZADO_QUEUE = "usuario.actualizado.queue";

    // Routing keys
    public static final String USUARIO_REGISTRADO_ROUTING_KEY = "usuario.registrado";
    public static final String USUARIO_ACTUALIZADO_ROUTING_KEY = "usuario.actualizado";

    // Exchange
    @Bean
    public TopicExchange usuarioExchange() {
        return new TopicExchange(USUARIO_EXCHANGE);
    }

    // Colas
    @Bean
    public Queue usuarioRegistradoQueue() {
        return new Queue(USUARIO_REGISTRADO_QUEUE, true);
    }

    @Bean
    public Queue usuarioActualizadoQueue() {
        return new Queue(USUARIO_ACTUALIZADO_QUEUE, true);
    }

    // Bindings
    @Bean
    public Binding usuarioRegistradoBinding() {
        return BindingBuilder
                .bind(usuarioRegistradoQueue())
                .to(usuarioExchange())
                .with(USUARIO_REGISTRADO_ROUTING_KEY);
    }

    @Bean
    public Binding usuarioActualizadoBinding() {
        return BindingBuilder
                .bind(usuarioActualizadoQueue())
                .to(usuarioExchange())
                .with(USUARIO_ACTUALIZADO_ROUTING_KEY);
    }

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}