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
import it.unibo.tearoom.SPRINT4.ui.model.states.BarmanState;

@Service
public class BarmanService extends ActorService {


    connQakCoap barmanConn;
    
	/*
	 * ---------------------------------------------------------- Client update on
	 * resource change to handle events from CoAP resource
	 * ---------------------------------------------------------- Update the page
	 * vie socket.io when the application-resource changes. Thanks to Eugenio Cerulo
	 */
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	public BarmanService(SimpMessagingTemplate msgTemp) {
		
	    System.out.println("&&&&&&&&&&& BARMAN SERVICE: trying to configure Barman connection");
	    barmanConn = new connQakCoap("localhost", "8070", "barman", "ctxbarman"  );  
	    barmanConn.createConnection();
	    
	    simpMessagingTemplate = msgTemp;
	    prepareUpdating();
	}   
	

	@ExceptionHandler 
	public ResponseEntity<String> handle(Exception ex) { 
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<String>("!!!!!!-----BarmanService ERROR " + ex.getMessage(), responseHeaders,
				HttpStatus.CREATED);
	}

	@Override
	protected void prepareUpdating() {
		barmanConn.getClient().observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				ObjectMapper mapper = new ObjectMapper(); 
				JsonNode msg = null;
				try {
					msg = mapper.readTree(response.getResponseText()); 
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				System.out.println("Barman Service --> CoapClient changed -> " + response.getResponseText());
				
				boolean busy = msg.get("busy").asBoolean();
				String preparingOrder = msg.get("PreparingOrder").asText();
				boolean orderReady = msg.get("OrderReady").asBoolean();
				int PreparingForTable = msg.get("PreparingForTable").asInt();
				int OrderReadyTable = msg.get("OrderReadyTable").asInt();
				
				if (busy == false) {
					BarmanState.getInstance().setCurrentTask("Waiting to get an order...");
				} 
				else if (PreparingForTable != -1 && !preparingOrder.equals("")) {
					BarmanState.getInstance().setCurrentTask("Recevied order of " + preparingOrder + " for table " + PreparingForTable + ". Preaparing...");
					BarmanState.getInstance().increaseOrdersReceived();
				}
				else if (OrderReadyTable != -1 && orderReady == true) {
					BarmanState.getInstance().setCurrentTask("Order Ready of " + preparingOrder + " for table " + PreparingForTable);
					BarmanState.getInstance().increaseTeasPreared();
					BarmanState.getInstance().increaseTeasReady();	
				}
				else {
					System.out.println("%%%%%%%%%% Wrong state has arrived! %%%%%%%%%%");
				}
				
				sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, BarmanState.getInstance());
			}

			@Override
			public void onError() {
				System.out.println("Barman Service --> CoapClient error!");
			}
		});
	}
	@Override
	public void sendUpdate() {
		simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForManager, BarmanState.getInstance());
	}

}
