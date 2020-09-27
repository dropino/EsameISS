package it.unibo.tearoom.SPRINT3.ui;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import connQak.configurator;
import connQak.connQakCoap;
import connQak.utils.ApplMessageUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;

@Controller
public class ClientController {
    String appName     		="tearoomGui";
    String viewModelRep		="startup";
    
    String robotHost = ""; //ConnConfig.hostAddr;		
    String robotPort = ""; //ConnConfig.port;

    String htmlPageMain  	= "client-view-main";
    String htmlPageTearoom 	= "client-view-tearoom";
    String htmlPageBadTemp 	= "client-view-bad-temp";
    
    connQakCoap smartbellConn;   
    connQakCoap waiterConn;
    
	public ClientController() {
	    configurator.configure();
	    htmlPageMain  = configurator.getPageTemplate();
	    robotHost =	configurator.getHostAddr();	
	    robotPort = configurator.getPort();
	
	    smartbellConn = new connQakCoap(robotHost, robotPort, configurator.getQakdest(), configurator.getCtxqadest()  );  
	    smartbellConn.createConnection();
 		
	    waiterConn = new connQakCoap(robotHost, robotPort, "waiter", configurator.getCtxqadest()  );  
	    waiterConn.createConnection();
	      
	 }
    
	 @GetMapping("/") 	 	 
	 public String entry(Model viewmodel) {
		  //viewmodel.addAttribute("arg", "Entry page loaded. Please use the buttons ");
	 	 return htmlPageMain;
	 } 
	   
	 @GetMapping("/tearoom")
	 public String getApplicationModelTearoom(Model viewmodel) {
		 preparePageUpdating();
		 return htmlPageTearoom; 
	 } 
	 
	 @GetMapping("/badtemp")
	 public String getApplicationModelBadTemperature(Model viewmodel) {
		 return htmlPageBadTemp;
	 } 
	 
	@ExceptionHandler 
	public ResponseEntity<String> handle(Exception ex) {
		HttpHeaders responseHeaders = new HttpHeaders();
	    return new ResponseEntity<String>(
	    		"RobotController ERROR " + ex.getMessage(), responseHeaders, HttpStatus.CREATED);
	}
	
	
	/* ----------------------------------------------------------
	   Client update on resource change to handle events from CoAP resource
	  ----------------------------------------------------------
     * Update the page vie socket.io when the application-resource changes. 
     * Thanks to Eugenio Cerulo
     */
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
	
	
	/* ----------------------------------------------------------
	   Message-handling Controller
	  ----------------------------------------------------------
	 */
	
	/*
	 * ringSmartbell
	 * returnType : ResourceRep with payload "redirection,ClientID,timeToWait" if reuqest is successful, "/badtemp" if it's not
	 * 
	 * This function buils a messege to send to the Smartbell to ask if the Customer's temperature is ok.
	 * The reply from the Smartbell is of type (TempStatus,ClientID), so we extract the payload and check:
	 * 		TempStatus: 0 failed - too high, 1 succeded - below 37.5
	 * 		if 0 we redirect the client to the page showing a message to tell him he cannot enter and has to leave.
	 * 
	 * 		if 1, we open the connection with the waiter and ask it the wait time. The answer will arrive in the form of (Time).
	 * 		Time: 0 - waiter can let the customer in right away, >0 the Customer has to wait.
	 * 		if 0, the client has to wait for the Waiter to deploy him to the table, so we answer with redirection to /tearoom and clientID
	 * 			which handles the UI for that.
	 * 		if >0 we send back a message with ClientID , time to wait and the javascript will handle the view to show the countdown.
	 * 
	 * 	If the Client got in or is waiting in the hall, he's now only in contact with the waiter, so the message mapping will follow /waiter.
	 * */
	@MessageMapping("/smartbell")
	@SendTo("/topic/display")
	public ServerReply ringSmartbell() throws Exception {
	 		ApplMessage ringMsg = MsgUtil.buildRequest("web", "ringBell", "ringBell(ok)", "smartbell" );
	 		
	 		ApplMessage reply = smartbellConn.request( ringMsg );  
			System.out.println("------------------- Controller appl message reply content p =" + reply.msgContent()  );
			
			String[] ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);
			
			if (ringRepArgs[0].compareTo("0") == 0)
			{
				System.out.println("------------------- Controller: respond with bad temperature redir"  );
				return new  ServerReply("badtemp","0","0");
			}
			else {
			  //on tempStatus msg the clientId is the second argument, so idx = 1
				System.out.println("------------------- Controller appl message content =" + ringRepArgs[1]  );
		 		ApplMessage askWaitTime = MsgUtil.buildRequest("web", "waitTime", "waitTime(" + ringRepArgs[1] + ")", "smartbell" );
		 		ApplMessage timeToWait = waiterConn.request( askWaitTime );   
		 		
		 		String ttw = ApplMessageUtils.extractApplMessagePayload(timeToWait, 0); 

				System.out.println("------------------- Controller answer to client =" + ringRepArgs[1] + "," + ttw  );

				return new  ServerReply("tearoom",ringRepArgs[1], ttw);   
				
			}	
	}
	
	@MessageMapping("/waiter") 
	@SendTo("/topic/display") 
	public ServerReply waiterInteraction(ClientRequest req) throws Exception {
		
		/*
		 *  Req ID:
		 *  	0) waitTime -> waitTime is automatically asked by the smartBell interaction function
		 *  	deploy*) 	deployEntrance, deployExit
		 *  	service*) 	requestOrder, requestPayment
		 *  	order) 		forwardOrder
		 *		pay) 		forwardPayment
		 * */

		if (req.getName().contains("deploy"))
			return askForDeployment(req);
		
		if (req.getName().contains("service"))
			return askForService(req);
		
		if (req.getName().compareTo("order") == 0)
		{
	 		ApplMessage msg = MsgUtil.buildDispatch("web", "order", "order(" + req.getPayload0() + ")", "waiter" );
	 		waiterConn.forward( msg ); 
	 		return new ServerReply("", "success");
		}
		
		if (req.getName().compareTo("pay") == 0)
		{
	 		ApplMessage msg = MsgUtil.buildDispatch("web", "pay", "pay(" + req.getPayload0() + ")", "waiter" );
	 		waiterConn.forward( msg );
	 		return new ServerReply("", "success");
		}
		
		return new ServerReply("", "error");
	}
	
	private ServerReply askForDeployment(ClientRequest req) {
 		ApplMessage msg = MsgUtil.buildRequest("web", "deploy", "deploy(" + req.getPayload0() + ","+ req.getPayload1() + ","+ req.getPayload2() + ")", "waiter" );
 		ApplMessage reply = waiterConn.request( msg );  
		System.out.println("------------------- Controller appl message reply content p =" + reply.msgContent()  );
		
		String[] repArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);
		
		return new ServerReply("", repArgs[0]);
	}
	
	private ServerReply askForService(ClientRequest req) {
 		ApplMessage msg = MsgUtil.buildRequest("web", "clientRequest", "clientRequest(" + req.getPayload0() + ","+ req.getPayload1() + ","+ req.getPayload2() + ")", "waiter" );
 		ApplMessage reply = waiterConn.request( msg );  
		System.out.println("------------------- Controller appl message reply content p =" + reply.msgContent()  );
				
		String[] repArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);
		
		return new ServerReply("", repArgs[0]);

	}
}
