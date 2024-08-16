package com.bangIt.blended.controller.user.payment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bangIt.blended.common.config.TossPaymentConfig;
import com.bangIt.blended.domain.dto.room.ReservationDTO;
import com.bangIt.blended.domain.dto.user.payment.PaymentRequestDTO;

import com.bangIt.blended.service.user.PaymentService;
import com.bangIt.blended.service.user.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final TossPaymentConfig tossPaymentConfig;
    private final PaymentService paymentService;
    private final ReservationService reservationService;

    @GetMapping
    public String paymentPage(Model model) {
        model.addAttribute("testClientApiKey", tossPaymentConfig.getTestClientApiKey());
        model.addAttribute("successUrl", tossPaymentConfig.getSuccessUrl());
        model.addAttribute("failUrl", tossPaymentConfig.getFailUrl());
        return "views/user/payment/payment";
    }

    @GetMapping("/reservation/{id}")
    @ResponseBody
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
    	ReservationDTO reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam String paymentKey,
                                 @RequestParam String orderId,
                                 @RequestParam Long amount,
                                 Model model) {
        // 결제 성공 처리 로직
        boolean isValid = paymentService.validatePayment(paymentKey, orderId, amount);
        if (isValid) {
            // 결제 정보를 DB에 저장
            paymentService.savePaymentInfo(paymentKey, orderId, amount);
            model.addAttribute("paymentKey", paymentKey);
            model.addAttribute("orderId", orderId);
            model.addAttribute("amount", amount);
            return "views/user/payment/success";
        } else {
            return "redirect:/payment/fail";
        }
    }

    @GetMapping("/fail")
    public String paymentFail(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "views/user/payment/fail";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        return paymentService.createPayment(paymentRequestDTO);
    }
}