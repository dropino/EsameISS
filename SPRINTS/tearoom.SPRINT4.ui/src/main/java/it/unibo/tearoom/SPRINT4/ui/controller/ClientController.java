package it.unibo.tearoom.SPRINT4.ui.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import connQak.configurator;
import connQak.connQakCoap;
import connQak.utils.ApplMessageUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.tearoom.SPRINT4.ui.model.ClientRequest;
import it.unibo.tearoom.SPRINT4.ui.model.ServerReply;

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
    
	/*
	 * ---------------------------------------------------------- Client update on
	 * resource change to handle events from CoAP resource
	 * ---------------------------------------------------------- Update the page
	 * vie socket.io when the application-resource changes. Thanks to Eugenio Cerulo
	 */
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
    
    /* Map to store ClientIDs-UUIDs correspondencies */
    private Map<String, String> userNames = new HashMap<>();
     
	public ClientController() {
	    configurator.configure();
	    htmlPageMain  = configurator.getPageTemplate();
	    robotHost =	configurator.getHostAddr();	
	    robotPort = configurator.getPort();  
	
	    System.out.println("&&&&&&&&&&& CLIENT CONTROLLER: trying to configure Smartbell connection");
	    smartbellConn = new connQakCoap(robotHost, "8071", configurator.getQakdest(), "ctxsmartbell"  );  
	    smartbellConn.createConnection();
 		
	    System.out.println("&&&&&&&&&&& CLIENT CONTROLLER: trying to configure Waiter connection");
	    waiterConn = new connQakCoap(robotHost, robotPort, "waiter", configurator.getCtxqadest()  );  
	    waiterConn.createConnection();
	      
	 }
    
	 @GetMapping("/main") 	 	 
	 public String entry(Model viewmodel) {
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
		return new ResponseEntity<String>("RobotController ERROR " + ex.getMessage(), responseHeaders,
				HttpStatus.CREATED);
	}

	private void preparePageUpdating() {
		waiterConn.getClient().observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				//convertire a Json e prendere CID
				if(response.getResponseText().contains("deliver-tea")) {
					System.out.println("ClientController --> CoapClient changed -> " + response.getResponseText());
					simpMessagingTemplate.convertAndSend("/user/topic/tearoom", 
							new ServerReply("", "delivery" ));
				}
			}

			@Override
			public void onError() {
				System.out.println("ClientController --> CoapClient error!");
			}
		});
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
	 * 
	 * 	If the Client got in or is waiting in the hall, he's now only in contact with the waiter, 
	 *  so the message mapping will follow /waiter.
	 * */
	@MessageMapping("/smartbell") 
	@SendToUser("/topic/main")
	
	public ServerReply ringSmartbell(@Header("simpSessionId") String sessionId, Principal principal) throws Exception {
	 		
		System.out.println("!!!!!------------------- principal name " + principal.getName()  );
		System.out.println("!!!!!------------------- header session id " + sessionId  );
		

		ApplMessage ringMsg = MsgUtil.buildRequest("web", "ringBell", "ringBell(ok)", "smartbell" );
	 		
	 		ApplMessage reply = smartbellConn.request( ringMsg );  
			System.out.println("------------------- Controller appl message reply RINGBELL p = " + reply.msgContent()  );
			
			String[] ringRepArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);
			
			if (ringRepArgs[0].compareTo("0") == 0)
			{
				System.out.println("------------------- Controller: respond with BAD TEMPERATURE REDIR"  );
				return new  ServerReply("badtemp","0","0");
			}
			else {
			  //on tempStatus msg the clientId is the second argument, so idx = 1
		 		ApplMessage askWaitTime = MsgUtil.buildRequest("web", "waitTime", "waitTime(" + ringRepArgs[1] + ")", "waiter" );
		 		ApplMessage timeToWait = waiterConn.request( askWaitTime ); 
		 		
				System.out.println("------------------- Controller appl message reply WAITTIME p = " + reply.msgContent()  );

		 		
		 		String ttw = ApplMessageUtils.extractApplMessagePayload(timeToWait, 0); 

				System.out.println("------------------- Controller ANSWER TO CLIENT = " + ringRepArgs[1] + ", " + ttw  );

				return new  ServerReply("tearoom", ringRepArgs[1], ttw);   
				
			}	
	}
	
	@MessageMapping("/waiter")   
	//@SendToUser("/topic/tearoom") 
	public void waiterInteraction(@Payload ClientRequest req, @Header("simpSessionId") String sessionId, Principal principal) throws Exception {
		System.out.println("!!!!!------------------- /app/waiter principal name " + principal.getName()  );
		System.out.println("!!!!!-------------------  /app/waiter header session id " + sessionId  );

		/*
		 * Req ID: 0) waitTime -> waitTime is automatically asked by the smartBell
		 * interaction function deploy*) deployEntrance, deployExit service*)
		 * requestOrder, requestPayment order) forwardOrder pay) forwardPayment
		 */
		
		ServerReply result = null;
		
		if (req.getName().contains("deploy")) {
			if (!this.userNames.containsKey(req.getPayload2()))
				this.userNames.put(req.getPayload2(), principal.getName());
			result = askForDeployment(req);
		}

		if (req.getName().contains("service"))
			result = askForService(req);

		if (req.getName().compareTo("order") == 0) {
			ApplMessage msg = MsgUtil.buildDispatch("web", "order", "order(" + req.getPayload0() + ")", "waiter");
			waiterConn.forward(msg);
			result = new ServerReply("", "success");
		}

		if (req.getName().compareTo("pay") == 0) {
			ApplMessage msg = MsgUtil.buildDispatch("web", "pay", "pay(" + req.getPayload0() + ")", "waiter");
			waiterConn.forward(msg);
			result = new ServerReply("", "success");
		}
		else
			result = new ServerReply("", "error");
		
		 ObjectMapper Obj = new ObjectMapper(); 
		  
	        try { 
	            String jsonStr = Obj.writeValueAsString(result); 
	  
	            // Displaying JSON String 
	            System.out.println(jsonStr); 
	        } catch (Exception e) { 
	            e.printStackTrace(); 
	        } 
	}

	private ServerReply askForDeployment(ClientRequest req) {
				
		ApplMessage msg = MsgUtil.buildRequest("web", "deploy",
				"deploy(" + req.getPayload0() + "," + req.getPayload1() + "," + req.getPayload2() + ")", "waiter");
		System.out.println("------------------- Controller sending deployment message msg =" + msg.toString());

		ApplMessage reply = waiterConn.request(msg);
		System.out.println("------------------- Controller appl message reply content p =" + reply.msgContent());

		String[] repArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);

		return new ServerReply("", repArgs[0]);
	}

	private ServerReply askForService(ClientRequest req) {
		ApplMessage msg = MsgUtil.buildRequest("web", "clientRequest",
				"clientRequest(" + req.getPayload0() + "," + req.getPayload1() + "," + req.getPayload2() + ")",
				"waiter");
		System.out.println("------------------- Controller sending service message msg =" + msg.toString());
		ApplMessage reply = waiterConn.request(msg);
		System.out.println("------------------- Controller appl message reply content p =" + reply.msgContent());

		String[] repArgs = ApplMessageUtils.extractApplMessagePayloadArgs(reply);

		return new ServerReply("", repArgs[0]);

	}
}
