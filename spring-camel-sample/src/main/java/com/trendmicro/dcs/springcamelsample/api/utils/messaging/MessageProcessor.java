package com.trendmicro.dcs.springcamelsample.api.utils.messaging;

import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springcamelsample.api.entity.Message;

@Component
public class MessageProcessor {

    public void print(Message message) {
        System.out.println(message.toString());
    }

}
