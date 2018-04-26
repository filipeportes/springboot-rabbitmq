package com.message.rabbitmq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SpringBootApplication
@ComponentScan("com.message.rabbitmq")
public class RabbitMQConsumer {

    private static List<String> messages = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(RabbitMQConsumer.class, args);
    }

    public static void addMessage(String message) {
        messages.add(message);
    }

    @ResponseBody
    @RequestMapping("/consumer")
    String consumer() {
        String consumed = messages.stream()
            .collect(Collectors.joining("<br/>"));

        return "<b>Mesages consumed:</b><br/><br/> " + consumed;
    }
}
