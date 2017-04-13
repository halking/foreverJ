package org.hal.forj.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receive {
	
	@JmsListener(destination="mailbox",containerFactory="myFactory")
	public void receive(Email email) {
		System.out.println("Receive <"+email+">");
	}
}	
