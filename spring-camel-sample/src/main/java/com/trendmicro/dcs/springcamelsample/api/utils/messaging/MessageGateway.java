package com.trendmicro.dcs.springcamelsample.api.utils.messaging;

import java.util.Collection;

import com.trendmicro.dcs.springcamelsample.api.entity.Message;

public interface MessageGateway {

	/**
	 * Sending one message
	 * @param message
	 */
	//<T extends Message> void send(T message);
	void send(Message message);
	
	/**
	 * Sending messages
	 * @param messages
	 */
	void send(Collection<Message> messages);

	/**
	 * Receive message
	 * @param message
	 * @return message
	 */
	Message receive(Message message);
	
	//TODO - void deleteMessage()
	
}
