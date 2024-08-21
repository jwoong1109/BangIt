package com.bangIt.blended.controller.user.payment;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
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
import com.bangIt.blended.domain.enums.PaymentMethod;
import com.bangIt.blended.service.user.PaymentService;
import com.bangIt.blended.service.user.ReservationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    // 의존성 주입: 결제 설정, 결제 서비스, 예약 서비스
    private final TossPaymentConfig tossPaymentConfig;
    private final PaymentService paymentService;
    private final ReservationService reservationService;

    // 1. 결제 생성 엔드포인트
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            // 결제 생성 로직 실행
            String response = paymentService.createPayment(paymentRequestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 콘솔에 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating payment: " + e.getMessage());
        }
    }

    // 2. 주문 ID를 생성하여 반환하는 엔드포인트
    @PostMapping("/generate-order-id")
    public ResponseEntity<Map<String, String>> generateOrderIdEndpoint(@RequestBody Map<String, Object> request) {
        String orderId = generateOrderId();  // 주문 ID 생성
        Map<String, String> response = new HashMap<>();
        response.put("orderId", orderId);
        return ResponseEntity.ok(response);
    }

    // 주문 ID 생성 로직
    private String generateOrderId() {
        SecureRandom random = new SecureRandom();
        StringBuilder orderId = new StringBuilder("order-");
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

        for (int i = 0; i < 20; i++) { // 20자 생성
            orderId.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }

        return orderId.toString();
    }

    // 3. 결제 페이지를 렌더링하는 엔드포인트
    @GetMapping("/payment")
    public String paymentPage(@RequestParam("reservationId") Long reservationId, Model model) {
        ReservationDTO reservationDTO = paymentService.getReservationById(reservationId);  // 예약 정보 조회

        // Custom orderId 생성
        String orderId = generateOrderId();

        // 날짜 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 몇 박인지 계산
        long nights = Duration.between(reservationDTO.getCheckInDate(), reservationDTO.getCheckOutDate()).toDays();

        // 가격 포맷팅
        double roomPrice = reservationDTO.getRoomPrice().doubleValue();
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedRoomPrice = df.format(roomPrice);

        double totalPrice = reservationDTO.getRoomPrice().doubleValue() * nights;
        String formattedTotalPrice = df.format(totalPrice);

        // 모델에 포맷된 날짜 추가
        model.addAttribute("formattedCheckInDate", reservationDTO.getCheckInDate().format(formatter));
        model.addAttribute("formattedCheckOutDate", reservationDTO.getCheckOutDate().format(formatter));
        model.addAttribute("nights", nights);
        model.addAttribute("formattedRoomPrice", formattedRoomPrice);
        model.addAttribute("formattedTotalPrice", formattedTotalPrice);
        // 모델에 reservationId와 orderId 추가
        model.addAttribute("reservation", reservationDTO);
        model.addAttribute("reservationId", reservationDTO.getId());
        model.addAttribute("orderId", orderId);
        model.addAttribute("testClientApiKey", tossPaymentConfig.getTestClientApiKey());
        model.addAttribute("successUrl", tossPaymentConfig.getSuccessUrl());
        model.addAttribute("failUrl", tossPaymentConfig.getFailUrl());

        return "views/user/payment/payment";  // 결제 페이지로 이동
    }

    // 4. 예약 ID를 이용해 금액을 가져오는 엔드포인트
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

    // 5. 특정 예약 정보를 JSON으로 반환하는 엔드포인트
    @GetMapping("/payment/reservation/{id}")
    @ResponseBody
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        logger.info("Fetching reservation with ID: {}", id);

        // Service를 통해 ReservationDTO를 가져옴
        ReservationDTO reservationDTO = paymentService.getReservationById(id);

        // DTO 반환
        return ResponseEntity.ok(reservationDTO);
    }

    // 6. 결제 성공 시 처리하는 엔드포인트
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("paymentKey") String paymentKey, @RequestParam("orderId") String orderId,
                                 @RequestParam("amount") Long amount, @RequestParam("reservationId") Long reservationId,
                                 @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                                 Model model) {
        try {
            // 결제 유효성 검사
            boolean isPaymentValid = paymentService.validatePayment(paymentKey, orderId, amount);

            if (isPaymentValid) {
                // 결제 정보 저장
                paymentService.savePaymentInfo(paymentKey, orderId, amount, reservationId, paymentMethod);
                // 예약 상태를 COMPLETED로 업데이트
                reservationService.updateReservationStatusToCompleted(reservationId);

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

    // 7. 결제 실패 시 처리하는 엔드포인트
    @GetMapping("/fail")
    public String paymentFail(@RequestParam String message, @RequestParam String code,
                              @RequestParam(required = false) Long reservationId, // reservationId 추가
                              Model model) {

        logger.error("Payment failed - message: {}, code: {}, reservationId: {}", message, code, reservationId);

        model.addAttribute("message", "결제가 실패했습니다: " + message);
        model.addAttribute("code", code);

        if (reservationId != null) {
            model.addAttribute("reservationId", reservationId);
        }

        return "views/user/payment/fail";
    }

    // 8. 결제 정보를 저장하는 엔드포인트
    @PostMapping("/savePaymentInfo")
    public ResponseEntity<String> savePaymentInfo(@RequestBody PaymentRequestDTO requestDTO) {
        try {
            // 서비스 레이어로 데이터를 전달하여 결제 정보 저장 로직을 처리
            paymentService.savePaymentInfo(requestDTO.getPaymentKey(), requestDTO.getOrderId(), requestDTO.getAmount(),
                    requestDTO.getReservationId(), requestDTO.getPaymentMethod());

            return ResponseEntity.ok("Payment information saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving payment information: " + e.getMessage());
        }
    }
}
