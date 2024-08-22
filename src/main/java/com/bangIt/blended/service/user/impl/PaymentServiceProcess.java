package com.bangIt.blended.service.user.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bangIt.blended.common.config.TossPaymentConfig;
import com.bangIt.blended.domain.dto.room.ReservationDTO;
import com.bangIt.blended.domain.dto.user.payment.PaymentRequestDTO;
import com.bangIt.blended.domain.entity.PaymentEntity;
import com.bangIt.blended.domain.entity.ReservationEntity;
import com.bangIt.blended.domain.enums.PaymentMethod;
import com.bangIt.blended.domain.enums.PaymentStatus;
import com.bangIt.blended.domain.repository.PaymentEntityRepository;
import com.bangIt.blended.domain.repository.ReservationEntityRepository;
import com.bangIt.blended.service.user.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceProcess implements PaymentService {

    // 의존성 주입: 결제 설정, 예약 리포지토리, 결제 리포지토리
    private final TossPaymentConfig tossPaymentConfig;
    private final ReservationEntityRepository reservationRepository;
    private final PaymentEntityRepository paymentRepository;

    // 1. 고유한 주문 ID를 생성하는 메서드
    private String generateOrderId() {
        return UUID.randomUUID().toString();  // UUID를 사용하여 고유한 주문 ID 생성
    }

    // 2. 결제 요청을 생성하는 메서드
    @Override
    @Transactional
    public String createPayment(PaymentRequestDTO requestDTO) {
        StringBuilder responseBody = new StringBuilder();
        String orderId = generateOrderId(); // 주문 ID 생성
        try {
            Long reservationId = requestDTO.getReservationId();
            ReservationEntity reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + reservationId));

            log.info("Generated Order ID: {}", orderId);

            // 기존에 동일한 주문 ID가 있는지 확인
            PaymentEntity existingPayment = paymentRepository.findByOrderId(orderId).orElse(null);

            if (existingPayment == null) {
                // 결제 엔티티 생성 및 저장
                PaymentEntity payment = PaymentEntity.builder().orderId(orderId).reservation(reservation)
                        .amount(requestDTO.getAmount()).paymentDate(LocalDateTime.now())
                        .paymentMethod(requestDTO.getPaymentMethod()) // DTO에서 PaymentMethod 가져오기
                        .paymentStatus(PaymentStatus.PENDING).build();

                paymentRepository.save(payment);
                log.info("Payment entity saved with orderId: {}", payment.getOrderId());

                // 토스 결제 API를 호출하여 결제 요청 전송
                URL url = tossPaymentConfig.getPaymentUrl().toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                // 요청 본문 생성
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("orderNo", orderId);
                jsonBody.put("amount", requestDTO.getAmount());
                jsonBody.put("amountTaxFree", requestDTO.getAmountTaxFree());
                jsonBody.put("productDesc", requestDTO.getProductDesc());
                jsonBody.put("apiKey", tossPaymentConfig.getTestClientApiKey());
                jsonBody.put("autoExecute", requestDTO.isAutoExecute());
                jsonBody.put("resultCallback", requestDTO.getResultCallback());
                jsonBody.put("retUrl", requestDTO.getRetUrl());
                jsonBody.put("retCancelUrl", requestDTO.getRetCancelUrl());

                log.info("Creating payment with request body: {}", jsonBody);

                // 결제 요청 전송
                try (BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream())) {
                    bos.write(jsonBody.toString().getBytes(StandardCharsets.UTF_8));
                    bos.flush();
                }

                // 결제 요청에 대한 응답 처리
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        responseBody.append(line);
                    }
                }

                log.info("Payment creation response: {}", responseBody);
            } else {
                log.info("Payment with orderId {} already exists.", orderId);
                responseBody.append("Payment already exists.");
            }
        } catch (Exception e) {
            log.error("Error creating payment", e);
            responseBody.append(e.getMessage());
        }

        // 주문 ID와 응답 본문을 함께 반환
        return orderId + "|" + responseBody.toString();
    }

    // 3. 결제 유효성을 검사하는 메서드 (현재는 더미 로직으로 구현됨)
    @Override
    public boolean validatePayment(String paymentKey, String orderId, Long amount) {
        try {
            log.info("Validating payment - paymentKey: {}, orderId: {}, amount: {}", paymentKey, orderId, amount);

            JSONObject requestBody = new JSONObject();
            requestBody.put("orderId", orderId);
            requestBody.put("amount", amount);
            requestBody.put("paymentKey", paymentKey);

            log.info("Payment validation request body: {}", requestBody);

            // TODO: 실제 유효성 검사 로직 구현 필요
            return true;
        } catch (Exception e) {
            log.error("Error validating payment", e);
            return false;
        }
    }

    // 4. 결제 정보를 저장하는 메서드
    @Override
    @Transactional
    public void savePaymentInfo(String paymentKey, String orderId, Long amount, Long reservationId,
            PaymentMethod paymentMethod) {
        log.info(
                "Saving payment information - paymentKey: {}, orderId: {}, amount: {}, reservationId: {}, paymentMethod: {}",
                paymentKey, orderId, amount, reservationId, paymentMethod);

        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + reservationId));

        PaymentEntity payment = paymentRepository.findByOrderId(orderId).orElseGet(() -> {
            log.warn("Payment not found with Order ID: {}. Creating new payment entity.", orderId);
            return PaymentEntity.builder().orderId(orderId).reservation(reservation) // ReservationEntity 설정
                    .amount(amount).paymentStatus(PaymentStatus.PENDING).paymentMethod(paymentMethod) // PaymentMethod 설정
                    .build();
        });

        payment.updatePaymentInfo(paymentKey, PaymentStatus.COMPLETED, amount);
        paymentRepository.save(payment);

        log.info("Payment information saved successfully for orderId: {}", orderId);
    }

    // 5. 예약 정보를 조회하는 메서드
    @Transactional(readOnly = true)
    public ReservationDTO getReservationById(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));
        return ReservationDTO.fromEntity(reservation);
    }

    // 6. 예약 금액을 조회하는 메서드
    @Transactional(readOnly = true)
    public Long getAmountByReservationId(Long reservationId) {
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + reservationId));
        return reservation.getRoom().getRoomPrice();
    }
}
