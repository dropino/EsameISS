package it.unibo.tearoom.SPRINT4.ui;

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
import it.unibo.tearoom.SPRINT4.ui.ServerReply;
import it.unibo.tearoom.SPRINT4.ui.WebSocketConfig;

@Controller
public class ManagerController {
	String appName = "managerTearoomGui";
	String viewModelRep = "startup";

	String robotHost = ""; // ConnConfig.hostAddr;
	String robotPort = ""; // ConnConfig.port;

	// DA CREARE FILE
	String htmlPageMain = "manager-view-main";

	connQakCoap smartbellConn;
	connQakCoap waiterConn;
	connQakCoap barmanConn;

	actorQakCoapObserver obs;

	public ManagerController() {
		configurator.configure();
		htmlPageMain = configurator.getPageTemplate();
		robotHost = configurator.getHostAddr();
		robotPort = configurator.getPort();

		smartbellConn = new connQakCoap(robotHost, robotPort, "smartbell", configurator.getCtxqadest());
		smartbellConn.createConnection();

		waiterConn = new connQakCoap(robotHost, robotPort, "waiter", configurator.getCtxqadest());
		waiterConn.createConnection();

		barmanConn = new connQakCoap(robotHost, robotPort, "barman", configurator.getCtxqadest());
		barmanConn.createConnection();
	}

	@GetMapping("manager/")
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
				if (busy == false) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "listening"));
				}
				// client arrives and gets told to wait for waitTime
				else if (busy == true && waitTime >= 0) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "wait: " + waitTime));
				}
				// handleDeploy
				else if (busy == true && !clientID.equals("") && table != -1 && !movingTo.equals("")
						&& !receivedRequest.equals("deploy")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, new ServerReply("",
							"receivedRequest " + receivedRequest + ", table: " + table + ", clientID: " + clientID));
				}
				// transfer drink order
				else if (!order.equals("")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "order: " + order));
				}
				// arriva richiesta pulire tavolo
				else if (busy == true && table != -1 && receivedRequest == "tableDirty" && !movingTo.equals("")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "cleaning table: " + table));
				}
				// pulisci tavolo
				else if (busy == true && table != -1 && receivedRequest == "tableDirty" && movingTo.equals("")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "cleaning table: " + table));
				}
				// get drink
				else if (busy == true && table != -1 && receivedRequest.equals("drinkReady")
						&& movingTo.equals("barman")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "drink ready for table: " + table));
				}
				// bring drink
				else if (busy == true && table != -1 && receivedRequest.equals("bringDrink")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "bringing drink to table: " + table));
				}
				// leave drink at table
				else if (waitTime != -1) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "brought drink at table: " + table + " max wait time: " + waitTime));
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

				if (msg.get("busy").asBoolean() == true) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "checkTemp"));
				} else if (msg.get("busy").asBoolean() == true && msg.get("ClientArrived").asBoolean() == true
						&& msg.get("ClientDenied").asInt() != -1) {
					int CID = msg.get("ClientDenied").asInt();
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "TempKO" + CID));
				} else if (msg.get("ClientArrived").asBoolean() == false && msg.get("ClientAccepted").asInt() != -1) {
					int CID = msg.get("ClientAccepted").asInt();
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "TempOK" + CID));
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
				msg.get("order_ready");
				if (msg.get("busy").asBoolean() == false && msg.get("PreparingForTable").asInt() == -1
						&& msg.get("PreparingOrder").asText().equals("") && msg.get("OrderReadyTable").asInt() == -1
						&& msg.get("OrderReady").asBoolean() == false) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "waitForOrder"));
				} else if (msg.get("busy").asBoolean() == true && msg.get("PreparingForTable").asInt() != -1
						&& msg.get("PreparingOrder").asText() != "") {
					int TABLE = msg.get("PreparingForTable").asInt();
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "preparingOrder for table: " + TABLE));
				} else if (msg.get("PreparingForTable").asInt() == -1 && msg.get("PreparingOrder").asText().equals("")
						&& msg.get("OrderReadyTable").asInt() != -1 && msg.get("OrderReady").asBoolean() == true) {
					int TABLE = msg.get("OrderReadyTable").asInt();
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient,
							new ServerReply("", "orderReady for table: " + TABLE));
				}
			}

			@Override
			public void onError() {
				System.out.println("ClientController --> CoapClient error!");
			}
		});
	}

}
