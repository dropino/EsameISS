package it.unibo.tearoom.SPRINT4.ui.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.unibo.tearoom.SPRINT4.ui.services.BarmanService;
import it.unibo.tearoom.SPRINT4.ui.services.SmartbellService;
import it.unibo.tearoom.SPRINT4.ui.services.WaiterService;

@Controller
public class ManagerController {
	
	String htmlPageMain = "manager-view-main";
	
    private final SmartbellService smartbellService;
    private final WaiterService waiterService;
    private final BarmanService barmanService;
    
	public ManagerController(SmartbellService smartbellService, WaiterService waiterService, BarmanService barmanService) {
		this.barmanService = barmanService;
		this.smartbellService = smartbellService;
	    this.waiterService = waiterService;
	 }
    
	 @GetMapping("/manager") 	 	 
	 public String entry(Model viewmodel) {
				 
	 	 return htmlPageMain;
	 }
	 
	 @MessageMapping("/manager")
	 public void updateAll() {
		smartbellService.sendUpdate();
		waiterService.sendUpdate();
		barmanService.sendUpdate();
		
	} 

}