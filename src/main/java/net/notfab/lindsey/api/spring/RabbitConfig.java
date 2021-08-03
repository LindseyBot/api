package net.notfab.lindsey.api.spring;

import net.lindseybot.utils.RabbitUtils;
import net.notfab.lindsey.shared.rpc.services.RemoteGuildsService;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.remoting.client.AmqpProxyFactoryBean;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class RabbitConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(this.jackson2JsonMessageConverter());
        template.setReplyTimeout(TimeUnit.SECONDS.toMillis(15));
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return RabbitUtils.jacksonConverter();
    }

    /**
     * Provides the connection to the Remote-Guilds service, implemented on the gateway.
     *
     * @param template RabbitMQ Template.
     * @return Proxy for the Service.
     */
    @Bean
    public AmqpProxyFactoryBean remoteGuildsService(RabbitTemplate template) {
        return RabbitUtils.createRemoteService(RemoteGuildsService.class, template);
    }

}
