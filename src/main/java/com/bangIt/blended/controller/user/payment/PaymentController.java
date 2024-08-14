package com.bangIt.blended.controller.user.payment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bangIt.blended.common.config.TossPaymentConfig;
import com.bangIt.blended.domain.dto.user.payment.PaymentRequestDTO;
import com.bangIt.blended.service.user.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequiredArgsConstructor
public class PaymentController {

	private final TossPaymentConfig tossPaymentConfig;
	private final PaymentService paymentService;
	
	 @GetMapping("/payment")
	    public String paymentPage(Model model) {
	        model.addAttribute("testClientApiKey", tossPaymentConfig.getTestClientApiKey());
	        model.addAttribute("successUrl", tossPaymentConfig.getSuccessUrl());
	        model.addAttribute("failUrl", tossPaymentConfig.getFailUrl());
        return "views/user/payment/payment";
    }
	
	@GetMapping("/success")
	public String paymentSuccess() {
		return "views/user/payment/success"; // `success.html`을 렌더링
	}
	
	 @GetMapping("/fail")
	    public String paymentfail() {
	        return "views/user/payment/fail"; // `fail.html`을 렌더링
	    }

	@PostMapping("/payment")
	public String PaymentStart(PaymentRequestDTO requestDTO) {
		String paymentUrl = paymentService.createPayment(requestDTO);
		 return "redirect:" + paymentUrl;
	}
	
	
	
	
	
	
}
