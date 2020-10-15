package it.unibo.tearoom.SPRINT4.ui.services;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

@Service
public class SmartbellService {

    connQakCoap smartbellConn;

    
	/*
	 * ---------------------------------------------------------- Client update on
	 * resource change to handle events from CoAP resource
	 * ---------------------------------------------------------- Update the page
	 * vie socket.io when the application-resource changes. Thanks to Eugenio Cerulo
	 */
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
    
//	static SmartbellService service = null;
//
//	public static SmartbellService getInstance() {
//		if (service == null)
//			service = new SmartbellService();
//		
//		return service;
//
//	}

	public SmartbellService(SimpMessagingTemplate msgTemp) {
		
	    System.out.println("&&&&&&&&&&& SMARTBELL SERVICE: trying to configure Smartbell connection");
	    smartbellConn = new connQakCoap("localhost", "8071", configurator.getQakdest(), "ctxsmartbell"  );  
	    smartbellConn.createConnection();
	    
	    simpMessagingTemplate = msgTemp;
	}   
	

	/*
	 * ------------------ Message-handling Controller ----------------------
	 */

	/*
	 * ringSmartbell returnType : ResourceRep with payload
	 * "redirection,ClientID,timeToWait" if reuqest is successful, "/badtemp" if
	 * it's not
	 * 
	 * This function buils a messege to send to the Smartbell to ask if the
	 * Customer's temperature is ok. The reply from the Smartbell is of type
	 * (TempStatus,ClientID), so we extract the payload and check: TempStatus: 0
	 * failed - too high, 1 succeded - below 37.5 if 0 we redirect the client to the
	 * page showing a message to tell him he cannot enter and has to leave.
	 * 
	 * if 1, we open the connection with the waiter and ask it the wait time. The
	 * answer will arrive in the form of (Time). Time: 0 - waiter can let the
	 * customer in right away, >0 the Customer has to wait. if 0, the client has to
	 * wait for the Waiter to deploy him to the table, so we answer with redirection
	 * to /tearoom and clientID which handles the UI for that. if >0 we send back a
	 * message with ClientID , time to wait and the javascript will handle the view
	 * to show the countdown.
	 * */
	public void executeService(WaiterService waiterService, String UUID) {
		
		ServerReply result = null;

		ApplMessage ringMsg = MsgUtil.buildRequest("web", "ringBell", "ringBell(ok)", "smartbell" );
	 		
	 		ApplMessage reply = smartbellConn.request( ringMsg );  
			System.out.println("------------------- Controller appl message reply RINGBELL p = " + reply.msgContent()  );
			
			String[] ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);
			
			if (ringRepArgs[0].compareTo("0") == 0)
			{
				System.out.println("------------------- Controller: respond with BAD TEMPERATURE REDIR"  );
				result =  new ServerReply("/badtemp","0","0");
			}
			else {

			  //on tempStatus msg the clientId is the second argument, so idx = 1
		 		ApplMessage askWaitTime = MsgUtil.buildRequest("web", "waitTime", "waitTime(" + ringRepArgs[1] + ")", "waiter" );
		 		ApplMessage timeToWait = waiterService.executeSmartbellMessage( askWaitTime ); 
		 		
				System.out.println("------------------- Controller appl message reply WAITTIME p = " + reply.msgContent()  );

		 		
		 		String ttw = ApplMessageUtils.extractApplMessagePayload(timeToWait, 0); 

				System.out.println("------------------- Controller ANSWER TO CLIENT = " + ringRepArgs[1] + ", " + ttw  );

				result = new  ServerReply("/tearoom", ringRepArgs[1], ttw);   
			
			}	
			
		    simpMessagingTemplate.convertAndSendToUser(UUID, WebSocketConfig.topicForClientMain, result);

	}

	public void prepareUpdating() {
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
			boolean ClientDenied = msg.get("ClientDenied").asBoolean();
			boolean ClientAccepted = msg.get("ClientAccepted").asBoolean();
			
			if (busy == true) {
				System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
				sendUpdate("smartBell");
			} 
			else if (busy == true && ClientArrived == true && ClientDenied == true) {
				SmartBellState.getInstance().setClientsProcessed(SmartBellState.getInstance().getClientsProcessed()+1);
				System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
				sendUpdate("smartBell");
				
			} 
			else if (ClientArrived == false && ClientAccepted == false) {
				SmartBellState.getInstance().setClientsProcessed(SmartBellState.getInstance().getClientsProcessed()+1);
				SmartBellState.getInstance().setClientsAdmitted(SmartBellState.getInstance().getClientsAdmitted()+1);
				System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
				sendUpdate("smartBell");
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
					new ServerReply("", sender, WaiterState.getInstance()));			
		}
		else if(sender.equals("smartBell")){
			simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForManager,
					new ServerReply("", sender, SmartBellState.getInstance()));			
		}
		else if(sender.equals("barman")) {
			simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForManager,
					new ServerReply("", sender, BarmanState.getInstance()));			
		}
		else {
			System.out.println("%%%%%%%%%%%%%% managerController - Sender not recognized! %%%%%%%%%%%%%%");
		}
	}
}
