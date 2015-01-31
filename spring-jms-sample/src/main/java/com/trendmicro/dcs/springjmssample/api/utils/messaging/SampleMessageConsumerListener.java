package com.trendmicro.dcs.springjmssample.api.utils.messaging;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.springjmssample.api.utils.messaging.processor.SampleMessageConsumer;

@Component
public class SampleMessageConsumerListener implements MessageListener {

	@Autowired
	SampleMessageConsumer consumer;
	
	@Override
    public void onMessage(Message message) {
        try {
            if (message != null) {
                System.out.println("Received: " + ((TextMessage) message).getText());
                consumer.execute(message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
