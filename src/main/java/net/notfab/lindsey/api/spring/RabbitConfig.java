package net.notfab.lindsey.api.spring;

import net.notfab.lindsey.shared.rpc.CustomAmqpProxyFactory;
import net.notfab.lindsey.shared.rpc.services.RemoteGuilds;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.remoting.client.AmqpProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "RPC";
    public static final String ROUT_KEY = "discord.key";
    public static final String QUEUE_NAME = "Discord";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setExchange(EXCHANGE);
        rabbitTemplate.setReplyTimeout(30000);
        rabbitTemplate.setUserCorrelationId(true);
        rabbitTemplate.setRoutingKey(ROUT_KEY);
        return rabbitTemplate;
    }

    @Bean
    public AmqpProxyFactoryBean amqpProxyFactoryBean(RabbitTemplate rabbitTemplate) {
        AmqpProxyFactoryBean amqpProxyFactoryBean = new CustomAmqpProxyFactory();
        amqpProxyFactoryBean.setAmqpTemplate(rabbitTemplate);
        amqpProxyFactoryBean.setServiceInterface(RemoteGuilds.class);
        amqpProxyFactoryBean.setRoutingKey(ROUT_KEY);
        return amqpProxyFactoryBean;
    }

}
