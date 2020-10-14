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
    String appName     		="managerTearoomGui";
    String viewModelRep		="startup";
    
    String robotHost = ""; //ConnConfig.hostAddr;		
    String robotPort = ""; //ConnConfig.port;

    //DA CREARE FILE
    String htmlPageMain  	= "manager-view-main";
    
    connQakCoap smartbellConn;   
    connQakCoap waiterConn;
    connQakCoap barmanConn;
    
    actorQakCoapObserver obs;
    
	public ManagerController() {
	    configurator.configure();
	    htmlPageMain  = configurator.getPageTemplate();
	    robotHost =	configurator.getHostAddr();	
	    robotPort = configurator.getPort();
	
	    smartbellConn = new connQakCoap(robotHost, robotPort, "smartbell", configurator.getCtxqadest()  );  
	    smartbellConn.createConnection();
 		
	    waiterConn = new connQakCoap(robotHost, robotPort, "waiter", configurator.getCtxqadest()  );  
	    waiterConn.createConnection();
	    
	    barmanConn = new connQakCoap(robotHost, robotPort, "barman", configurator.getCtxqadest()  );  
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
				
				if(response.getResponseText().contains("listening")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "listening"));
				}
				else if(response.getResponseText().contains("Client_must_wait")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "Client_must_wait"));
				}
				else if(response.getResponseText().contains("waiter_arrived")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "waiter_arrived"));
				}
				else if(response.getResponseText().contains("waiter_rdy_leave")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "waiter_rdy_leave"));
				}
				else if(response.getResponseText().contains("waiter_rdy_getDrink")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "waiter_rdy_getDrink"));
				}
				else if(response.getResponseText().contains("deliver-tea-$CTABLE")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "deliver-tea-$CTABLE"));
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
				if(response.getResponseText().contains("Discard")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "Discard"));
				}
				else if(response.getResponseText().contains("Accept")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "Accept"));
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
				//STILL MISSING FROM QAK
				if(response.getResponseText().contains("making-tea")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "making-tea"));
				}
			}

			@Override
			public void onError() {    
				System.out.println("ClientController --> CoapClient error!");
			}
		});
	}

	
}
