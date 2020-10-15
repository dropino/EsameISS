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

@Service
public class WaiterService {
    connQakCoap waiterConn;
    /* Map to store ClientIDs-UUIDs correspondencies */
    private Map<String, String> userNames = new HashMap<>();

    
	/*
	 * ---------------------------------------------------------- Client update on
	 * resource change to handle events from CoAP resource
	 * ---------------------------------------------------------- Update the page
	 * vie socket.io when the application-resource changes. Thanks to Eugenio Cerulo
	 */
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

    
//    static WaiterService service = null;
//
//	public static WaiterService getInstance() {
//		if (service == null)
//			service = new WaiterService();
//		
//		return service;
//
//	}
    
	private WaiterService(SimpMessagingTemplate msgTemp) {
		super();
 		
	    System.out.println("&&&&&&&&&&& WAITER SERVICE: trying to configure Waiter connection");
	    waiterConn = new connQakCoap("localhost", "8072", "waiter", configurator.getCtxqadest()  );  
	    waiterConn.createConnection();
	      
	    simpMessagingTemplate = msgTemp;
	}

	
	 
	@ExceptionHandler 
	public ResponseEntity<String> handle(Exception ex) { 
		HttpHeaders responseHeaders = new HttpHeaders();
		return new ResponseEntity<String>("RobotController ERROR " + ex.getMessage(), responseHeaders,
				HttpStatus.CREATED);
	}

	public void prepareUpdating() {
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
	
	public void executeClientService(ClientRequest req, String UUID) {
		
		ServerReply result = null;
		
		if (!this.userNames.containsKey(req.getClientid())) {
			this.userNames.put(req.getClientid(), UUID);
		}
		
		if (req.getName().contains("deploy")) {

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
	
		System.out.println("------------------- WaiterService ANSWER TO CLIENT = " + result.getPayload0() );
    
	    simpMessagingTemplate.convertAndSendToUser(this.userNames.get(req.getClientid()), WebSocketConfig.topicForClientInTearoom, result);

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
