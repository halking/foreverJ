package org.hal.forj.jms;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

public class JmsMain {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-jms.xml");
		JmsQueueSender sender = (JmsQueueSender)context.getBean("send");
		sender.sendToQueue();
		sender.simpleSend();
//		sender.sendMessageWithConvert();
	}
}
