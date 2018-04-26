package com.message.rabbitmq.common;

import java.io.Serializable;

public class SimpleMessage implements Serializable {

    private int id;
    private String description;

    public SimpleMessage() {
    }

    public SimpleMessage(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
