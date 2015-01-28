package com.trendmicro.dcs.springcamelsample.api.utils.messaging;

import java.util.Collection;

import com.trendmicro.dcs.springcamelsample.api.entity.Message;

public interface MessageGateway {

	/**
	 * Sending one message
	 * @param message
	 */
	void sendMessage(Message message);
	
	/**
	 * Sending messages
	 * @param messages
	 */
	void sendMessage(Collection<Message> messages);

	/**
	 * Receive message
	 * @param message
	 * @return message
	 */
	Message receiveMessage(Message message);
	
	//TODO - void deleteMessage()
	
}
