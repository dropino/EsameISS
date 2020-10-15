package it.unibo.tearoom.SPRINT4.ui.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.unibo.tearoom.SPRINT4.ui.model.ClientRequest;
import it.unibo.tearoom.SPRINT4.ui.services.SmartbellService;
import it.unibo.tearoom.SPRINT4.ui.services.WaiterService;

@Controller
public class ClientController {
    String appName     		="tearoomGui"; 
    String viewModelRep		="startup";
    
    String robotHost = ""; //ConnConfig.hostAddr;		
    String robotPort = ""; //ConnConfig.port;

    String htmlPageMain  	= "client-view-main";
    String htmlPageTearoom 	= "client-view-tearoom";
    String htmlPageBadTemp 	= "client-view-bad-temp";
    
    private final SmartbellService smartbellService;
    private final WaiterService waiterService;
             
	public ClientController(SmartbellService smartbellService, WaiterService waiterService) {
		this.smartbellService = smartbellService;
	    this.waiterService = waiterService;
	 }
    
	 @GetMapping("/main") 	 	 
	 public String entry(Model viewmodel) {
	 	 return htmlPageMain;
	 } 
	   
	 @GetMapping("/main/tearoom")
	 public String getApplicationModelTearoom(Model viewmodel) { 
		 waiterService.prepareUpdating();
		 return htmlPageTearoom; 
	 } 
	 
	 @GetMapping("/main/badtemp")
	 public String getApplicationModelBadTemperature(Model viewmodel) {
		 return htmlPageBadTemp;
	 } 

	@MessageMapping("/smartbell") 
	public void ringSmartbell(@Header("simpSessionId") String sessionId, Principal principal) throws Exception {
	 		
		System.out.println("!!!!!------------------- principal name " + principal.getName()  );
		System.out.println("!!!!!------------------- header session id " + sessionId  );
		
		this.smartbellService.executeService(waiterService, principal.getName());
		
		
	}
	
	@MessageMapping("/waiter")   
	public void waiterInteraction(@Payload ClientRequest req, @Header("simpSessionId") String sessionId, Principal principal) throws Exception {
		System.out.println("!!!!!------------------- /app/waiter principal name " + principal.getName()  );
		System.out.println("!!!!!-------------------  /app/waiter header session id " + sessionId  );
	
		this.waiterService.executeClientService(req, principal.getName());
	}
		

}
