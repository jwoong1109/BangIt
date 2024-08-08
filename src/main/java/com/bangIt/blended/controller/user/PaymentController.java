package com.bangIt.blended.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PaymentController {

	@GetMapping("/payment")
	public String Payment() {
		return "views/user/payment/payment";
	}
	
	
}
