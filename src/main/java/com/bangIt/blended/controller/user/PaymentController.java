package com.bangIt.blended.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bangIt.blended.service.user.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequiredArgsConstructor
public class PaymentController {

	//private final PaymentService paymentService;
	
	@GetMapping("/payment")
	public String Payment() {
		return "views/user/payment/payment";
	}
	
	/*
	 * @PostMapping("path") public String postMethodName(@RequestBody String entity)
	 * { //TODO: process POST request
	 * 
	 * return entity; }
	 */
	
	
	
	
	
}
