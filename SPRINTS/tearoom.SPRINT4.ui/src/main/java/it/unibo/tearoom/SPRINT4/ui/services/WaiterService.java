package it.unibo.tearoom.SPRINT4.ui.services;

import java.util.HashMap;
import java.util.Map;

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

import connQak.configurator;
import connQak.connQakCoap;
import connQak.utils.ApplMessageUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.tearoom.SPRINT4.ui.config.WebSocketConfig;
import it.unibo.tearoom.SPRINT4.ui.model.ClientRequest;
import it.unibo.tearoom.SPRINT4.ui.model.ServerReply;
import it.unibo.tearoom.SPRINT4.ui.model.states.BarmanState;
import it.unibo.tearoom.SPRINT4.ui.model.states.SmartbellState;
import it.unibo.tearoom.SPRINT4.ui.model.states.WaiterState;

@Service
public class WaiterService  extends ActorService {
    connQakCoap waiterConn;
    
    Map<String, String> users = new HashMap<String, String>();
        
	/*
	 * ---------------------------------------------------------- Client update on
	 * resource change to handle events from CoAP resource
	 * ---------------------------------------------------------- Update the page
	 * vie socket.io when the application-resource changes. Thanks to Eugenio Cerulo
	 */
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
    
	private WaiterService(SimpMessagingTemplate msgTemp) {
		super();
 		
	    System.out.println("&&&&&&&&&&& WAITER SERVICE: trying to configure Waiter connection");
	    waiterConn = new connQakCoap("localhost", "8072", "waiter", configurator.getCtxqadest()  );  
	    waiterConn.createConnection();
	      
	    simpMessagingTemplate = msgTemp;
	    
	    prepareUpdating();
	}
	
	 
	@ExceptionHandler 
	public ResponseEntity<String> handle(Exception ex) { 
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<String>("RobotController ERROR " + ex.getMessage(), responseHeaders,
				HttpStatus.CREATED);
	}

	@Override
	protected void prepareUpdating() {
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
				int payment = msg.get("payment").asInt();
				int waitTime = msg.get("waitTime").asInt();
				String movingTo = msg.get("movingTo").asText();
				String movingFrom = msg.get("movingFrom").asText();
				String receivedRequest = msg.get("receivedRequest").asText();
				
				// listening
				if (busy == false && movingTo.equals("")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				//going home
				else if (busy == false && movingTo.equals("home")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				//answer time 
				else if (busy == true && waitTime != -1) {
					if(waitTime == 0) {
						SmartbellState.getInstance().increaseClientsAdmitted();
						WaiterState.getInstance().decreaseFreeTables();
					}
					else {
						SmartbellState.getInstance().increaseClientsWaiting();
					}
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, SmartbellState.getInstance());	
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());				
				}
				// handleDeploy entrance
				else if (busy == true && !clientID.equals("") && table != -1 && !movingTo.equals("")
						&& !receivedRequest.equals("DeployEntrance")) {
					WaiterState.getInstance().increaseDeployedToTable();
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				// handleDeploy exit
				else if (busy == true && !clientID.equals("") && table != -1 && !movingTo.equals("")
						&& !receivedRequest.equals("DeployExit")) {
					WaiterState.getInstance().increaseDeployedToExit();
					WaiterState.getInstance().increaseDirtyTables();
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				// drink arrives to client
				else if (receivedRequest.equals("drinkReady")) {
					WaiterState.getInstance().increaseTeasDelivered();
					BarmanState.getInstance().decreaseTeasReady();
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager,  WaiterState.getInstance());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, BarmanState.getInstance());
					//send update to client waiting for her drink
					simpMessagingTemplate.convertAndSendToUser(users.get(clientID), WebSocketConfig.topicForClientInTearoom, new ServerReply("", "delivery"));
				}
				// pulisci tavolo
				else if (busy == true && table != -1 && receivedRequest == "tableDirty" && movingTo.equals("")) {
					WaiterState.getInstance().decreaseDirtyTables();
					WaiterState.getInstance().increaseFreeTables();
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				//pagamento
				else if (receivedRequest.equals("Pay") && busy == true && payment > 0) {
					WaiterState.getInstance().increaseEarnings(payment);
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager,  WaiterState.getInstance());
				}
			
			}

			@Override
			public void onError() {
				System.out.println("ClientController --> CoapClient error!");
			}
		});
	}
	
	public void executeClientService(ClientRequest req, String UUID) {
		
		ServerReply result = null;
		
		this.users.put(req.getClientid(), UUID);
		
		
		if (req.getName().contains("deploy")) {

			result = askForDeployment(req);
		}

		else if (req.getName().contains("service"))
			result = askForService(req);

		else if (req.getName().compareTo("order") == 0) {
			ApplMessage msg = MsgUtil.buildDispatch("web", "order", "order(" + req.getPayload0() + ")", "waiter");
			waiterConn.forward(msg);
			result = new ServerReply("", "success");
		}

		else if (req.getName().compareTo("pay") == 0) {
			ApplMessage msg = MsgUtil.buildDispatch("web", "pay", "pay(" + req.getPayload0() + ")", "waiter");
			waiterConn.forward(msg);
			result = new ServerReply("", "success");
		}
		else
			result = new ServerReply("", "error");  
	
		System.out.println("------------------- WaiterService ANSWER TO CLIENT = " + result.getPayload0() );
    
	    simpMessagingTemplate.convertAndSendToUser(UUID, WebSocketConfig.topicForClientInTearoom, result);

	}
	
	public ApplMessage executeSmartbellMessage(ApplMessage msg) {
		return waiterConn.request( msg ); 
	}
	
	private ServerReply askForDeployment(ClientRequest req) {
		
		ApplMessage msg = MsgUtil.buildRequest("web", "deploy",
				"deploy(" + req.getPayload0() + "," + req.getPayload1() + "," + req.getClientid() + ")", "waiter");
		System.out.println("------------------- Controller sending deployment message msg =" + msg.toString());

		ApplMessage reply = waiterConn.request(msg);
		System.out.println("------------------- Controller appl message reply content p =" + reply.msgContent());

		String[] repArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);

		return new ServerReply("", repArgs[0]);
	}
 
	private ServerReply askForService(ClientRequest req) {
		ApplMessage msg = MsgUtil.buildRequest("web", "clientRequest",
				"clientRequest(" + req.getPayload0() + "," + req.getPayload1() + "," + req.getClientid() + ")",
				"waiter");
		System.out.println("------------------- Controller sending service message msg =" + msg.toString());
		ApplMessage reply = waiterConn.request(msg);
		System.out.println("------------------- Controller appl message reply content p =" + reply.msgContent());

		String[] repArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);

		return new ServerReply("", repArgs[0]);

	}
}
