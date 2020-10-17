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
public class WaiterService extends ActorService {
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
		waiterConn = new connQakCoap("localhost", "8072", "waiter", configurator.getCtxqadest());
		waiterConn.createConnection();

		simpMessagingTemplate = msgTemp;

		prepareUpdating();
	}

	@ExceptionHandler
	public ResponseEntity<String> handle(Exception ex) {
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<String>("Waiter Service ERROR " + ex.getMessage(), responseHeaders,
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

				System.out.println("Waiter Service --> CoapClient changed -> " + response.getResponseText());

				boolean busy = msg.get("busy").asBoolean();
				String clientID = msg.get("clientID").asText();
				int table = msg.get("table").asInt();
				String order = msg.get("order").asText();
				int payment = msg.get("payment").asInt();
				int waitTime = msg.get("waitTime").asInt();
				String movingTo = msg.get("movingTo").asText();
				String movingFrom = msg.get("movingFrom").asText();
				String receivedRequest = msg.get("receivedRequest").asText();
				boolean acceptedWaiting = msg.get("acceptedWaiting").asBoolean();
				String arrival = msg.get("arrival").asText();
				boolean tableDirty = msg.get("tableDirty").asBoolean();

				// listening
				if (busy == false) {
					// going home
					WaiterState.getInstance().setCurrentTask("Listening");
					if (movingTo.equals("home")) {
						WaiterState.getInstance().setCurrentMovement("Going home");
					} else {
						WaiterState.getInstance().setCurrentMovement("Chillin'");

					}
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				else if(arrival.equals("error")) {
					WaiterState.getInstance().setCurrentMovement("ERROR WHILE MOVING");
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				// answer time
				else if (receivedRequest.equals("waitTime")) { 
					if (waitTime == 0) {
						if (acceptedWaiting == true) { // if the client accepted was one that was waiting...
							SmartbellState.getInstance().decreaseClientsWaiting();
						}
						WaiterState.getInstance().setCurrentTask("Accepted Client " + clientID);
						SmartbellState.getInstance().increaseClientsAdmitted();
						WaiterState.getInstance().decreaseFreeTables();
					} else { // client told to wait outside
						WaiterState.getInstance().setCurrentTask("Told Client " + clientID + " they have to wait");
						SmartbellState.getInstance().increaseClientsWaiting();
					}
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, SmartbellState.getInstance());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				// handleDeploy entrance
				else if (receivedRequest.equals("deployEntrance")) {
					if (movingTo.equals("entrancedoor")) {
						WaiterState.getInstance()
								.setCurrentTask("Received request to deploy client " + clientID + " to table");
						WaiterState.getInstance().setCurrentMovement("Moving to entrance door");
					} else if (movingTo.contains("table")) {
						WaiterState.getInstance().setCurrentTask(
								"Deploying client " + clientID + " from entrance door to table " + table);
						WaiterState.getInstance().increaseDeployedToTable();
					}
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				// handleDeploy exit
				else if (receivedRequest.equals("deployExit")) {
					if (!arrival.equals("exitdoor")) {
						WaiterState.getInstance().setCurrentTask("Deploying client " + clientID + " to exit door");
						WaiterState.getInstance()
								.setCurrentMovement("Moving from table " + table + " to " + " exit door");
						WaiterState.getInstance().increaseDirtyTables();
					} else if (arrival.equals("exitdoor")) {
						WaiterState.getInstance().setCurrentTask("Deployed client " + clientID + " to exit door");
						WaiterState.getInstance().setCurrentMovement("Arrived at exit door");
						WaiterState.getInstance().increaseDeployedToExit();
					}
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				// receives request to order
				else if (receivedRequest.equals("order")) {
					if (movingTo.contains("table")) {
						WaiterState.getInstance().setCurrentTask("Received order request from client " + clientID);
						WaiterState.getInstance().setCurrentMovement("Going to table " + table);
					} else if (arrival.contains("table")) {
						WaiterState.getInstance().setCurrentTask("Taking order from client " + clientID);
						WaiterState.getInstance().setCurrentMovement("Arrived at table " + table);
					} else {
						WaiterState.getInstance().setCurrentTask(
								"Client " + clientID + " ordered " + order + ". Sending order to barman");
						WaiterState.getInstance().setCurrentMovement("Arrived at table " + table);
					}
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				// receives request to pay
				else if (receivedRequest.equals("pay")) {
					if (movingTo.contains("table")) {
						WaiterState.getInstance().setCurrentTask("Received payment request from client " + clientID);
						WaiterState.getInstance().setCurrentMovement("Going to table " + table);
					} else if (arrival.contains("table")) {
						WaiterState.getInstance().setCurrentTask("Taking payment from client " + clientID);
						WaiterState.getInstance().setCurrentMovement("Arrived at table " + table);
					} else {
						WaiterState.getInstance().setCurrentTask("Client " + clientID + " paid " + payment);
						WaiterState.getInstance().setCurrentMovement("Arrived at table " + table);
						WaiterState.getInstance().increaseEarnings(payment);
					}
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				// pulisci tavolo
				else if (receivedRequest.equals("tableDirty")) {    
					if(movingTo.equals("")) {// already at the table
						WaiterState.getInstance().setCurrentTask("Received request to clean table "+table);
						WaiterState.getInstance().setCurrentMovement("Going to table "+table);
					}
					else if(arrival.contains("table")){
						WaiterState.getInstance().setCurrentTask("Cleaning table "+table);
						WaiterState.getInstance().setCurrentMovement("Arrived at table "+table);
					}
					else if(tableDirty == false) {
						WaiterState.getInstance().setCurrentTask("Finished cleaning table "+table);
						WaiterState.getInstance().setCurrentMovement("Arrived at table "+table);
						WaiterState.getInstance().decreaseDirtyTables();
						WaiterState.getInstance().increaseFreeTables();
					}
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
				}
				// drink arrives to client
				else if (receivedRequest.equals("drinkReady")) {
					System.out.println("Waiter Service: DrinkReady arrival value: " + arrival);
					if(movingTo.equals("barman")) {
						WaiterState.getInstance().setCurrentTask("Received drink ready alert from barman");
						WaiterState.getInstance().setCurrentMovement("Going to barman");
					} else if (movingFrom.equals("barman")) {
						WaiterState.getInstance().setCurrentTask("Got drink from barman");
						WaiterState.getInstance().setCurrentMovement("Going to table " + table);
						BarmanState.getInstance().decreaseTeasReady();
					}
					else if(arrival.contains("table")) {
						System.out.println("Waiter Service: DrinkReady arrived for delivery clientID " + clientID + "(" + users.get(clientID) + ") at table " + table);
						WaiterState.getInstance().setCurrentTask("Brought tea to client " + clientID);
						WaiterState.getInstance().setCurrentMovement("Arrived at table " + table);
						WaiterState.getInstance().increaseTeasDelivered();
						System.out.println("Waiter Service: DrinkReady arrived for delivery clientID " + clientID + "(" + users.get(clientID) + ") at table " + table);
						System.out.println("Waiter Service: DrinkReady sending PERSONAL update");
						simpMessagingTemplate.convertAndSendToUser(users.get(clientID),WebSocketConfig.topicForClientInTearoom, new ServerReply("", Integer.toString(waitTime)));
					}
					System.out.println("Waiter Service: DrinkReady sending STATE update");
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, WaiterState.getInstance());
					sendUpdate(simpMessagingTemplate, WebSocketConfig.topicForManager, BarmanState.getInstance());
				}
			}

			@Override
			public void onError() {
				System.out.println("Waiter Service --> CoapClient error!");
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
		} else
			result = new ServerReply("", "error");

		System.out.println("------------------- WaiterService ANSWER TO CLIENT = " + result.getPayload0());

		simpMessagingTemplate.convertAndSendToUser(UUID, WebSocketConfig.topicForClientInTearoom, result);

	}

	public ApplMessage executeSmartbellMessage(ApplMessage msg) {
		return waiterConn.request(msg);
	}

	private ServerReply askForDeployment(ClientRequest req) {

		ApplMessage msg = MsgUtil.buildRequest("web", "deploy",
				"deploy(" + req.getPayload0() + "," + req.getPayload1() + "," + req.getClientid() + ")", "waiter");
		System.out.println("------------------- Waiter Service sending deployment message msg =" + msg.toString());

		ApplMessage reply = waiterConn.request(msg);
		System.out.println("------------------- Waiter Service appl message reply content p =" + reply.msgContent());

		String[] repArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);

		return new ServerReply("", repArgs[0]);
	}

	private ServerReply askForService(ClientRequest req) {
		ApplMessage msg = MsgUtil.buildRequest("web", "clientRequest",
				"clientRequest(" + req.getPayload0() + "," + req.getPayload1() + "," + req.getClientid() + ")",
				"waiter");
		System.out.println("------------------- Waiter Service sending service message msg =" + msg.toString());
		ApplMessage reply = waiterConn.request(msg);
		System.out.println("------------------- Waiter Service appl message reply content p =" + reply.msgContent());

		String[] repArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);

		return new ServerReply("", repArgs[0]);
	}

	@Override
	public void sendUpdate() {
		simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForManager, WaiterState.getInstance());
	}
}
