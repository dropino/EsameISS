package it.unibo.tearoom.SPRINT4.ui.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public abstract class ActorService {
	
	
	abstract protected void prepareUpdating();
	
	protected void sendUpdate(SimpMessagingTemplate simpMessagingTemplate, String destination, Object msgPayload) {
		simpMessagingTemplate.convertAndSend(destination, msgPayload);			

	}
	
	abstract public void sendUpdate();
}
