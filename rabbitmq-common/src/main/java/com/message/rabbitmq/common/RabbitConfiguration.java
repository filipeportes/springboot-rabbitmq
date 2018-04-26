package com.message.rabbitmq.common;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    public static final String TOPIC_QUEUE = "topic.queue";
    public static final String TTL_QUEUE = "ttl.queue";
    public static final String DLX_QUEUE = "dlx.queue";
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String DLX_EXCHANGE = "dlx.exchange";
    public static final String TEST_TOPIC = "topic";

    @Bean
    FanoutExchange newDeadLetterExchange() {
        return (FanoutExchange) ExchangeBuilder.fanoutExchange(DLX_EXCHANGE)
            .durable(true)
            .build();
    }

    @Bean
    Queue newQueue() {
        return QueueBuilder
            .durable(TOPIC_QUEUE)
            .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
            .build();
    }

    @Bean
    Exchange newTopicExchange() {
        return ExchangeBuilder.topicExchange(TOPIC_EXCHANGE)
            .durable(true)
            .build();
    }

    @Bean
    Binding newBinding() {
        return BindingBuilder.bind(newQueue())
            .to(newTopicExchange())
            .with(TEST_TOPIC)
            .noargs();
    }

    @Bean
    Queue newTTLQueueWithDeadLetter() {
        return QueueBuilder
            .durable(TTL_QUEUE)
            .withArgument("x-message-ttl", 30000)
            .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
            .build();
    }

    @Bean
    Binding newTTLBinding() {
        return BindingBuilder.bind(newTTLQueueWithDeadLetter())
            .to(newTopicExchange())
            .with(TEST_TOPIC)
            .noargs();
    }

    @Bean
    Queue newDeadLetterQueue() {
        return QueueBuilder
            .durable(DLX_QUEUE)
            //lazy queues move their contents to disk as early as practically possible,
            // and only load them in RAM when requested by consumers
            .withArgument("x-queue-mode", "lazy")
            .build();
    }

    @Bean
    Binding newDLXBinding() {
        return BindingBuilder.bind(newDeadLetterQueue())
            .to(newDeadLetterExchange());
    }

}
