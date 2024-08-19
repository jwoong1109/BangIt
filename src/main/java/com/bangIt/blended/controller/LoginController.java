package com.bangIt.blended.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
