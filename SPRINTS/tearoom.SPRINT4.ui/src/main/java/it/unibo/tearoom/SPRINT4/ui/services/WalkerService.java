package it.unibo.tearoom.SPRINT4.ui.services;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import connQak.connQakCoap;
import it.unibo.tearoom.SPRINT4.ui.config.WebSocketConfig;
import it.unibo.tearoom.SPRINT4.ui.model.states.WaiterState;

@Service
public class WalkerService extends ActorService {


    connQakCoap walkerConn;

    
	/*
	 * ---------------------------------------------------------- Client update on
	 * resource change to handle events from CoAP resource
	 * ---------------------------------------------------------- Update the page
	 * vie socket.io when the application-resource changes. Thanks to Eugenio Cerulo
	 */
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	public WalkerService(SimpMessagingTemplate msgTemp) {
		
	    System.out.println("&&&&&&&&&&& WALKER SERVICE: trying to configure Walker connection");
	    walkerConn = new connQakCoap("localhost", "8050", "walker", "ctxwalker");  
	    walkerConn.createConnection();
	    
	    simpMessagingTemplate = msgTemp;
	    
	    prepareUpdating();
	}   

	@ExceptionHandler 
	public ResponseEntity<String> handle(Exception ex) { 
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<String>("!!!!!!-----WalkerService ERROR " + ex.getMessage(), responseHeaders,
				HttpStatus.CREATED);
	}

	@Override
	protected void prepareUpdating() {
		walkerConn.getClient().observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				ObjectMapper mapper = new ObjectMapper(); 
				JsonNode msg = null;
				try {
					msg = mapper.readTree(response.getResponseText());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				int positionX = msg.get("positionX").asInt();
				int positionY = msg.get("positionY").asInt();
				
				WaiterState.getInstance().setPosition(positionX, positionY);
				
				sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());

			}

			@Override
			public void onError() {
				System.out.println("WalkerService --> CoapClient error!");
			}
		});
	}
	
	@Override
	public void sendUpdate() {
		simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForManager, WaiterState.getInstance());
	}

}
