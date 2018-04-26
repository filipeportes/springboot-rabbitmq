package com.message.rabbitmq.producer;

import com.message.rabbitmq.common.SimpleMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicInteger;

import static com.message.rabbitmq.common.RabbitConfiguration.TEST_TOPIC;
import static com.message.rabbitmq.common.RabbitConfiguration.TOPIC_EXCHANGE;

@Controller
@SpringBootApplication
@ComponentScan("com.message.rabbitmq")
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) {
        SpringApplication.run(RabbitMQProducer.class, args);
    }

    @ResponseBody
    @RequestMapping("/producer")
    String producer() {
        int id = counter.incrementAndGet();
        SimpleMessage message = new SimpleMessage(id, "Simple Message id " + id);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, TEST_TOPIC, message);

        return "Mesage Produced with id: " + message.getId();
    }
}
