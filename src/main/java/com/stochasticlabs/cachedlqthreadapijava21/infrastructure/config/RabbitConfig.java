package com.stochasticlabs.cachedlqthreadapijava21.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Value("${app.messaging.input-queue}")
    private String inputQueueName;

    @Value("${app.messaging.dlq-queue}")
    private String dlqQueueName;

    private static final String EXCHANGE_NAME = "stochastic-exchange";
    private static final String DLQ_EXCHANGE_NAME = "stochastic-exchange.DLQ";
    private static final String ROUTING_KEY = "stochastic-routing-key";

    private final ObjectMapper objectMapper;

    public RabbitConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public TopicExchange mainExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange dlqExchange() {
        return new TopicExchange(DLQ_EXCHANGE_NAME);
    }

    @Bean
    public Queue mainQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DLQ_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", ROUTING_KEY);

        return QueueBuilder.durable(inputQueueName)
                .withArguments(arguments)
                .build();
    }

    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(dlqQueueName).build();
    }

    @Bean
    public Binding bindMainQueue() {
        return BindingBuilder.bind(mainQueue())
                .to(mainExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding bindDlqQueue() {
        return BindingBuilder.bind(dlqQueue())
                .to(dlqExchange())
                .with(ROUTING_KEY);
    }
}
