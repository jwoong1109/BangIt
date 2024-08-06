package com.bangIt.blended.controller.chatbot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ChatbotController {
	
	@GetMapping("/chatbot")
	public String list() {
		return "views/chatbot/chatbot";
	}
	
	
}
