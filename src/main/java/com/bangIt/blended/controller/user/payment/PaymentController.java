package com.bangIt.blended.controller.user.payment;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bangIt.blended.common.config.TossPaymentConfig;
import com.bangIt.blended.domain.dto.room.ReservationDTO;
import com.bangIt.blended.domain.dto.user.payment.PaymentRequestDTO;
import com.bangIt.blended.domain.entity.ReservationEntity;
import com.bangIt.blended.service.user.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
@RequiredArgsConstructor
public class PaymentController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	private final TossPaymentConfig tossPaymentConfig;
	private final PaymentService paymentService;

	@GetMapping("/generate-order-id")
	public ResponseEntity<Map<String, String>> generateOrderIdEndpoint() {
		String orderId = generateOrderId();
		Map<String, String> response = new HashMap<>();
		response.put("orderId", orderId);
		return ResponseEntity.ok(response);
	}

	// 기존의 generateOrderId() 메소드를 그대로 사용
	private String generateOrderId() {
		SecureRandom random = new SecureRandom();
		StringBuilder orderId = new StringBuilder("order-");
		String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

		for (int i = 0; i < 20; i++) { // 20자 생성
			orderId.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
		}

		return orderId.toString();
	}

	@GetMapping("/payment")
	public String paymentPage(@RequestParam("reservationId") Long reservationId, Model model) {
		ReservationDTO reservationDTO = paymentService.getReservationById(reservationId);

		// Custom orderId 생성
		String orderId = generateOrderId();

		// 모델에 reservationId와 orderId 추가
		model.addAttribute("reservation", reservationDTO);
		model.addAttribute("reservationId", reservationDTO.getId());
		model.addAttribute("orderId", orderId);
		model.addAttribute("testClientApiKey", tossPaymentConfig.getTestClientApiKey());
		model.addAttribute("successUrl", tossPaymentConfig.getSuccessUrl());
		model.addAttribute("failUrl", tossPaymentConfig.getFailUrl());

		return "views/user/payment/payment";
	}

	@GetMapping("/payment/reservation/{id}")
	@ResponseBody
	public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
		logger.info("Fetching reservation with ID: {}", id);

		// Service를 통해 ReservationDTO를 가져옴
		ReservationDTO reservationDTO = paymentService.getReservationById(id);

		// DTO 반환
		return ResponseEntity.ok(reservationDTO);
	}

	@GetMapping("/getAmount")
	@ResponseBody
	public ResponseEntity<?> getAmount(@RequestParam("reservationId") Long reservationId) {
		try {
			// reservationId를 사용하여 예약 정보를 조회하고 금액을 가져옴
			Long amount = paymentService.getAmountByReservationId(reservationId);
			return ResponseEntity.ok(Collections.singletonMap("amount", amount));
		} catch (Exception e) {
			logger.error("Amount 조회 중 오류 발생: {}", reservationId, e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping("/success")
	public String paymentSuccess(
	        @RequestParam("paymentKey") String paymentKey, 
	        @RequestParam("orderId") String orderId,
	        @RequestParam("amount") Long amount,
	        @RequestParam("reservationId") Long reservationId,  // 필수 파라미터
	        Model model) {
	    try {
	        boolean isPaymentValid = paymentService.validatePayment(paymentKey, orderId, amount);

	        if (isPaymentValid) {
	            paymentService.savePaymentInfo(paymentKey, orderId, amount, reservationId); // reservationId 전달
	            model.addAttribute("message", "결제가 성공적으로 처리되었습니다.");
	        } else {
	            model.addAttribute("message", "결제 검증 실패. 고객센터로 문의해 주세요.");
	        }

	        model.addAttribute("paymentKey", paymentKey);
	        model.addAttribute("orderId", orderId);
	        model.addAttribute("amount", amount);

	        return "views/user/payment/success";
	    } catch (Exception e) {
	        logger.error("Error processing payment success for orderId: {}", orderId, e);
	        model.addAttribute("message", "결제 처리 중 오류가 발생했습니다. 고객센터로 문의해 주세요.");
	        return "views/user/payment/fail";
	    }
	}


	@GetMapping("/fail")
	public String paymentFail(@RequestParam String message, @RequestParam String code, Model model) {
		logger.error("Payment failed - message: {}, code: {}", message, code);
		model.addAttribute("message", "결제가 실패했습니다: " + message);
		model.addAttribute("code", code);
		return "views/user/payment/fail";
	}

	@PostMapping("/create")
	@ResponseBody
	public ResponseEntity<String> createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
	    try {
	        String response = paymentService.createPayment(paymentRequestDTO);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace(); // 예외를 콘솔에 출력
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error creating payment: " + e.getMessage());
	    }
	}

}
