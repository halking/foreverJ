package org.hal.forj.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ExampleListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				System.out.println("接收到一个文本消息：" + ((TextMessage) message).getText());
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		} else if (message instanceof MapMessage) {
				System.out.println("接收到一个MAP消息：" + ((MapMessage) message).toString());
		} else {
			throw new IllegalArgumentException("message must be is of type textMessage");
		}
	}

}
