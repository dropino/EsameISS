package it.unibo.robotWeb2020;

import java.util.Arrays;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
//import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.bind.annotation.RestController;

import connQak.configurator;
import connQak.connQakCoap;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import connQak.connQakCoap;

@RestController
public class Controller {
    String appName     ="robotGui";
    String viewModelRep="startup";
    String robotHost = ""; 		
    String robotPort = "";

    String htmlPageMain  = "client-view-main";
    String htmlPageTeaRoom  = "client-view-tearoom";
    
    connQakCoap connQakSupport ;

    
	public Controller() {
	    connQak.configurator.configure();
	    htmlPageMain  = connQak.configurator.getPageTemplate();
	    robotHost =	connQak.configurator.getHostAddr();	
	    robotPort = connQak.configurator.getPort();
	
	    connQakSupport = new connQakCoap();  
	    connQakSupport.createConnection();
	      
	 }
	
	
	/*
	 * Update the page vie socket.io when the application-resource changes.
	 */
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

    
	@GetMapping("/") 		 
	public String entry(Model viewmodel) {
		viewmodel.addAttribute("arg", "Entry page loaded. Please use the buttons ");
	 	peparePageUpdating();
	 	return htmlPageMain;
	} 
	
	
	@GetMapping("/") 		 
	public String getTeaRoom(Model viewmodel) {
		viewmodel.addAttribute("arg", "Entry page loaded. Please use the buttons ");
	 	peparePageUpdating();
	 	return htmlPageMain;
	} 
	
	
	@GetMapping("/") 		 
	public String entry(Model viewmodel) {
		viewmodel.addAttribute("arg", "Entry page loaded. Please use the buttons ");
	 	peparePageUpdating();
	 	return htmlPageMain;
	} 
	
	
	
	
	
	
	
	
	
	
	private void peparePageUpdating() {
		connQakSupport.getClient().observe(new CoapHandler() {
			@Override
			public void onLoad(CoapResponse response) {
				System.out.println("RobotController --> CoapClient changed ->" + response.getResponseText());
				simpMessagingTemplate.convertAndSend(WebSocketConfig.topicForClient, 
						new ResourceRep("" + HtmlUtils.htmlEscape(response.getResponseText())  ));
			}
	
			@Override
			public void onError() {
				System.out.println("RobotController --> CoapClient error!");
			}
		});
	}
	
}
