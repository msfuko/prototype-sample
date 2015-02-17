package com.trendmicro.dcs.jiramediator.api.dao;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.trendmicro.dcs.jiramediator.api.model.JiraResultBean;
import com.trendmicro.dcs.jiramediator.api.model.rest.AbstractBaseRequest;

@Component
public class JmsRequestDAO implements MessageListener, BaseRequestDAO {

	@Autowired
	private ConnectionFactory connectionFactory;

	@Autowired
	private String jmsRequestQueueName;
	
	@Autowired
	RestRequestDAO restRequestDAO;

	private Log logger = LogFactory.getLog(this.getClass());
	
	
	@Override
	public Object put(final AbstractBaseRequest request) {
		request.setRequestMethod(HttpMethod.POST);
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.send(jmsRequestQueueName, new MessageCreator() {
            public javax.jms.Message createMessage(Session session) throws JMSException {
            	javax.jms.Message jmsMessage;
            	/*
            	 * send message as Serialized Object
            	 */
            	jmsMessage = session.createObjectMessage(request); 
            	
            	return jmsMessage;
            }
    });
		return jmsTemplate;
	}
	
	@Override
	@JmsListener(containerFactory = "jmsListenerContainerFactory", destination = "jms_request")
	public void onMessage(Message jmsMessage) {
		String jmsMessageId = null;
				
		try {
			jmsMessageId = jmsMessage.getJMSMessageID();
			logger.info("New message is listened! - " + jmsMessageId);
			
			/*
			 * handling message
			 */
			AbstractBaseRequest receiveMessage = (AbstractBaseRequest)((ObjectMessage)jmsMessage).getObject();
			if (receiveMessage != null) {
				logger.info("New message is received! - " + jmsMessageId);
				//TODO error handling! send out notification
				JiraResultBean result = restRequestDAO.request(receiveMessage);
				logger.debug("New request is executed - " + result.getResponseContent());
			} else {
				System.err.println("Message invalid - Cannot get useful message " + jmsMessageId);
			}
		} catch (JMSException e) {
			System.err.println("Message invalid");
		}
	}

	@Override
	public JiraResultBean get(AbstractBaseRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
