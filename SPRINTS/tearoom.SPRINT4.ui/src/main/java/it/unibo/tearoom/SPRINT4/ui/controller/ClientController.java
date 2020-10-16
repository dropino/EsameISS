package it.unibo.tearoom.SPRINT4.ui.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
    String htmlPageBadReq 	= "client-view-bad-req";
    
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
		 return htmlPageTearoom; 
	 } 
	 
	 @GetMapping("/main/badreq")
	 public String getApplicationModelBadTemperature(Model viewmodel) {
		 return htmlPageBadReq;
	 } 

	@MessageMapping("/smartbell") 
	public void ringSmartbell(SimpMessageHeaderAccessor sha) throws Exception {
	 		
		System.out.println("!!!!!------------------- /app/smartbell principal name " + sha.getUser().getName()  );
		
		this.smartbellService.executeService(waiterService, sha.getUser().getName());
		
		
	}  
	
	@MessageMapping("/waiter")  
	public  void waiterInteraction(SimpMessageHeaderAccessor sha, @Payload ClientRequest req) throws Exception {
		System.out.println("!!!!!------------------- /app/waiter principal name " + sha.getUser().getName()  );
		
		this.waiterService.executeClientService(req, sha.getUser().getName());
	}
		

}
