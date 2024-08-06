package com.bangIt.blended.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class LoginController {

	
	@GetMapping("/login")
	public String login() {
		return "views/common/login";
	}
	
	@GetMapping("/partner-login")
	public String partnerLogin() {
		return "views/partner/partner-login";
	}
	
}
