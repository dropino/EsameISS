package it.unibo.tearoom.SPRINT4.ui;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import it.unibo.tearoom.SPRINT3.ui.ServerReply;
import it.unibo.tearoom.SPRINT3.ui.WebSocketConfig;

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
    
	@GetMapping("/") 	 	 
	public String entry(Model viewmodel) {
		return htmlPageMain;
	} 

	
	@MessageMapping("/update") 
	@SendTo("/topic/display/manager") 
	public ServerReply waiterInteraction(ClientRequest req) throws Exception {
		preparePageUpdating();
		ApplMessage barmanReply = getBarmanState();
		ApplMessage	smartBellReply = getSmartbellState();
		ApplMessage waiterReply = getWaiterState();
		
		String[] barArgs = ApplMessageUtils.extractApplMessagePayloadArgs(barmanReply);
		String[] bellArgs = ApplMessageUtils.extractApplMessagePayloadArgs(smartBellReply);
		String[] waiterArgs = ApplMessageUtils.extractApplMessagePayloadArgs(waiterReply);

		//TO DO: redir = ?
		return new ServerReply("redir?",barArgs[0], bellArgs[0], waiterArgs[0]);
	}
	
	
	private ApplMessage getBarmanState(){
 		ApplMessage getState = MsgUtil.buildRequest("web", "getstate", "getstate", "barman" );
		ApplMessage reply = barmanConn.request( getState );  
		return reply;	
	}
	
	private ApplMessage getSmartbellState(){
 		ApplMessage getState = MsgUtil.buildRequest("web", "getstate", "getstate", "smartbell" );
		ApplMessage reply = smartbellConn.request( getState );  
		return reply;	
	}

	private ApplMessage getWaiterState(){
 		ApplMessage getState = MsgUtil.buildRequest("web", "getstate", "getstate", "waiter" );
		ApplMessage reply = waiterConn.request( getState );  
		return reply;	
	}
	
	
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	
	private void preparePageUpdating() {
    	waiterConn.getClient().observe(new CoapHandler() { 
			@Override
			public void onLoad(CoapResponse response) {
				if(response.getResponseText().contains("deliver-tea")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
							new ServerReply("", "delivery"));
				}
			}

			@Override
			public void onError() {    
				System.out.println("ClientController --> CoapClient error!");
			}
		});
	}

	
}
