package it.unibo.tearoom.SPRINT4.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	String htmlPageMain = "home-view-main";

	public HomeController() {

	}

	@GetMapping("/")
	public String entry(Model viewmodel) {
		return htmlPageMain;
	}

}