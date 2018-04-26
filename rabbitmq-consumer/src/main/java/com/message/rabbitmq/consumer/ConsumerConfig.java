package com.message.rabbitmq.consumer;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.message.rabbitmq.common.RabbitConfiguration.TOPIC_QUEUE;

@Configuration
public class ConsumerConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    MessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer listener = new SimpleMessageListenerContainer();
        listener.setConnectionFactory(connectionFactory);
        listener.setDefaultRequeueRejected(false);
        listener.setMessageListener(
            m -> {
                String body = new String(m.getBody());

                //simulate an unexpected processing failure
                if (body.contains("id 5")) {
                    throw new RuntimeException("Processing Failure!");
                }
                RabbitMQConsumer.addMessage(body);
            }
        );
        listener.setQueueNames(TOPIC_QUEUE);

        return listener;
    }
}
