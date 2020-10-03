package it.unibo.tearoom.SPRINT4.ui;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import connQak.configurator;
import connQak.connQakCoap;
import connQak.utils.ApplMessageUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;

@Controller
public class ManagerController {
    String appName     		="tearoomGui";
    String viewModelRep		="startup";
    
    String robotHost = ""; //ConnConfig.hostAddr;		
    String robotPort = ""; //ConnConfig.port;

    //DA CREARE FILE
    String htmlPageMain  	= "manager-view-main";
    
    connQakCoap smartbellConn;   
    connQakCoap waiterConn;
    connQakCoap barmanConn;
    
	public ManagerController() {
	    configurator.configure();
	    htmlPageMain  = configurator.getPageTemplate();
	    robotHost =	configurator.getHostAddr();	
	    robotPort = configurator.getPort();
	
	    smartbellConn = new connQakCoap(robotHost, robotPort, configurator.getQakdest(), configurator.getCtxqadest()  );  
	    smartbellConn.createConnection();
 		
	    waiterConn = new connQakCoap(robotHost, robotPort, "waiter", configurator.getCtxqadest()  );  
	    waiterConn.createConnection();
	    
	    waiterConn = new connQakCoap(robotHost, robotPort, "barman", configurator.getCtxqadest()  );  
	    waiterConn.createConnection();
	      
	 }
    
	@GetMapping("/") 	 	 
	public String entry(Model viewmodel) {
		return htmlPageMain;
	} 

	
	@MessageMapping("/update") 
	@SendTo("/topic/display") 
	public ServerReply waiterInteraction(ClientRequest req) throws Exception {
		ApplMessage barmanReply = getBarmanState();
		ApplMessage	smartBellReply = getSmartbellState();
		ApplMessage waiterReply = getWaiterState();
		
		String[] barArgs = ApplMessageUtils.extractApplMessagePayloadArgs(barmanReply);
		String[] bellArgs = ApplMessageUtils.extractApplMessagePayloadArgs(smartBellReply);
		String[] waiterArgs = ApplMessageUtils.extractApplMessagePayloadArgs(waiterReply);

		//TO FIX: redir = ???
		return new ServerReply("???????",barArgs[0], bellArgs[0], waiterArgs[0]);
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
	
}
