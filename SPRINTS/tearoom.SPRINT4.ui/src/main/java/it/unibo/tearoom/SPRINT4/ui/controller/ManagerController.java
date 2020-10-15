package it.unibo.tearoom.SPRINT4.ui.controller;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import connQak.actorQakCoapObserver;
import connQak.configurator;
import connQak.connQakCoap;
import connQak.utils.ApplMessageUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.tearoom.SPRINT4.ui.config.WebSocketConfig;
import it.unibo.tearoom.SPRINT4.ui.model.BarmanState;
import it.unibo.tearoom.SPRINT4.ui.model.ServerReply;
import it.unibo.tearoom.SPRINT4.ui.model.SmartBellState;
import it.unibo.tearoom.SPRINT4.ui.model.WaiterState;

@Controller
public class ManagerController {
	String appName = "managerTearoomGui";
	String viewModelRep = "startup";

	String robotHost = ""; // ConnConfig.hostAddr;
	String robotPort = ""; // ConnConfig.port;

	String htmlPageMain = "manager-view-main";

	connQakCoap smartbellConn;
	connQakCoap waiterConn;
	connQakCoap barmanConn;

	actorQakCoapObserver obs;
	
	
	//DA MODIFICARE
	BarmanState barmanState = new BarmanState("0", "0", "0");
	SmartBellState smartBellState = new SmartBellState("0", "0", "0", "0");
	WaiterState waiterState = new WaiterState("0", "0", "0", "0", "0", "0");

	public ManagerController() {
		configurator.configure();
		htmlPageMain = configurator.getPageTemplate();
		robotHost = configurator.getHostAddr();
		robotPort = configurator.getPort();

//		smartbellConn = new connQakCoap(robotHost, "7071", "smartbell", "ctxsmartbell");
//		smartbellConn.createConnection();
//
//		waiterConn = new connQakCoap(robotHost, robotPort, "waiter", configurator.getCtxqadest());
//		waiterConn.createConnection();
//
//		barmanConn = new connQakCoap(robotHost, "7070", "barman",  "ctxbarman");
//		barmanConn.createConnection();
	}

	
	@GetMapping("/manager")
	public String entry(Model viewmodel) {
		preparePageUpdating();
		return htmlPageMain;
	}

	
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	
	private void preparePageUpdating() {
		waiterConn.getClient().observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode msg = null;
				try {
					msg = mapper.readTree(response.getResponseText());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				

				boolean busy = msg.get("busy").asBoolean();
				String clientID = msg.get("clientID").asText();
				int table = msg.get("table").asInt();
				String order = msg.get("order").asText();
				boolean payment = msg.get("payment").asBoolean();
				int waitTime = msg.get("waitTime").asInt();
				String movingTo = msg.get("movingTo").asText();
				String movingFrom = msg.get("movingFrom").asText();
				String receivedRequest = msg.get("receivedRequest").asText();
				
				// listening
				if (busy == false && movingTo.equals("")) {
					//FA COSE PER AGGIORNARE LO STATO
					//FA COSE PER AGGIORNARE LO STATO
					//FA COSE PER AGGIORNARE LO STATO
					//FA COSE PER AGGIORNARE LO STATO
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				//going home
				else if (busy == false && movingTo.equals("home")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				//????
				else if (busy == true && waitTime != -1) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");				
				}
				// client arrives and gets told to wait for waitTime
				else if (busy == true && waitTime >= 0) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				// handleDeploy entrance
				else if (busy == true && !clientID.equals("") && table != -1 && !movingTo.equals("")
						&& !receivedRequest.equals("DeployEntrance")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				// handleDeploy exit
				else if (busy == true && !clientID.equals("") && table != -1 && !movingTo.equals("")
						&& !receivedRequest.equals("DeployExit")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				// transfer drink order
				else if (!order.equals("")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				// arriva richiesta pulire tavolo
				else if (busy == true && table != -1 && receivedRequest == "tableDirty" && !movingTo.equals("")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				// pulisci tavolo
				else if (busy == true && table != -1 && receivedRequest == "tableDirty" && movingTo.equals("")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				// get drink
				else if (busy == true && table != -1 && receivedRequest.equals("drinkReady")
						&& movingTo.equals("barman")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				// bring drink
				else if (busy == true && table != -1 && receivedRequest.equals("bringDrink")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
				// leave drink at table
				else if (waitTime != -1) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("waiter");
				}
			}
			
			@Override
			public void onError() {
				System.out.println("ClientController --> CoapClient error!");
			}
		});

		
		
		
		
		smartbellConn.getClient().observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode msg = null;
				try {
					msg = mapper.readTree(response.getResponseText());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				boolean busy = msg.get("busy").asBoolean();
				boolean ClientArrived = msg.get("ClientArrived").asBoolean();
				int ClientDenied = msg.get("ClientDenied").asInt();
				int ClientAccepted = msg.get("ClientAccepted").asInt();
				
				if (busy == true) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("smartBell");

				} 
				else if (busy == true && ClientArrived == true
						&& ClientDenied != -1) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("smartBell");
					
				} 
				else if (ClientArrived == false && ClientAccepted != -1) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("smartBell");
				}
			}

			@Override
			public void onError() {
				System.out.println("ClientController --> CoapClient error!");
			}
		});

		
		
		
		
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
				
				boolean busy = msg.get("busy").asBoolean();
				String preparingOrder = msg.get("PreparingOrder").asText();
				boolean orderReady = msg.get("OrderReady").asBoolean();
				int PreparingForTable = msg.get("PreparingForTable").asInt();
				int OrderReadyTable = msg.get("OrderReadyTable").asInt();
				
				if (busy == false && PreparingForTable == -1
						&& preparingOrder.equals("") && OrderReadyTable  == -1
						&& orderReady == false) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("barman");
				} 
				else if (busy == true && PreparingForTable != -1
						&& !preparingOrder.equals("")) {
					barmanState.setOrdersReceived(barmanState.getOrdersReceived()+1);
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("barman");
					
				} 
				else if (PreparingForTable == -1 && preparingOrder.equals("")
						&& OrderReadyTable != -1 && orderReady == true) {
					barmanState.setTeasPreared(barmanState.getTeasPreared()+1);
					barmanState.setTeasReady(barmanState.getTeasReady()+1);
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate("barman");
				}
			}

			@Override
			public void onError() {
				System.out.println("ClientController --> CoapClient error!");
			}
		});
		
	}
	
	
	
	
	
	private void sendUpdate(String sender) {		
		if (sender.equals("waiter")){
			simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForManager,
					new ServerReply("", sender, waiterState));			
		}
		else if(sender.equals("smartBell")){
			simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForManager,
					new ServerReply("", sender, smartBellState));			
		}
		else if(sender.equals("barman")) {
			simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForManager,
					new ServerReply("", sender, barmanState));			
		}
	}

	
}