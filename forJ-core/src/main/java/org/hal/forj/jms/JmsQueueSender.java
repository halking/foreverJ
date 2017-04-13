package org.hal.forj.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;

public class JmsQueueSender extends AbstractSender {
	private JmsTemplate jmsTemplate;
	private Destination destination;
	private String queue;

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public void simpleSend() {
		System.out.println("-----开始发送一个文本消息-----");
		this.jmsTemplate.send(this.destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("this is a test!");
			}
		});
	}

	public void sendToQueue() {
		System.out.println("-----开始发送一个文本消息-----");
		this.jmsTemplate.send(this.queue, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("this is a test!");
			}
		});
	}

	public void sendMessageWithConvert() {
		System.out.println("-----开始发送一个MAP消息-----");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "steven");
		map.put("age", new Integer(23));
		this.jmsTemplate.convertAndSend(queue, map, new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws JMSException {
				message.setIntProperty("uid", 1234);
				message.setJMSCorrelationID("2781-0912");
				return message;
			}
		});
	}
}
