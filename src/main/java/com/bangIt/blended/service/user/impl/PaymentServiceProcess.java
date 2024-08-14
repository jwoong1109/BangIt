package com.bangIt.blended.service.user.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.bangIt.blended.common.config.TossPaymentConfig;
import com.bangIt.blended.domain.dto.user.payment.PaymentRequestDTO;
import com.bangIt.blended.service.user.PaymentService;

import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceProcess implements PaymentService {

    private final TossPaymentConfig tossPaymentConfig;

    @Override
    public String createPayment(PaymentRequestDTO requestDTO) {
        RestTemplate restTemplate = new RestTemplate();

        // JSON 요청 본문을 Map으로 생성
        Map<String, Object> body = new HashMap<>();
        body.put("orderId", requestDTO.getOrderId());
        body.put("amount", requestDTO.getAmount());
        body.put("product", requestDTO.getProdect());
        body.put("apiKey", tossPaymentConfig.getTestSecretApiKey());
        body.put("autoExecute", true);
        body.put("resultCallback", tossPaymentConfig.getSuccessUrl());
        body.put("retUrl", tossPaymentConfig.getSuccessUrl());
        body.put("retCancelUrl", tossPaymentConfig.getFailUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        // 결제 요청을 보내고 응답 받기
        ResponseEntity<Map> response = restTemplate.exchange(TossPaymentConfig.PAYMENT_URL, HttpMethod.POST, request, Map.class);

        // 응답에서 checkoutPage URL 추출
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("checkoutPage")) {
            return responseBody.get("checkoutPage").toString();
        }

        // checkoutPage URL이 없을 경우 처리 (예외 처리 등)
        throw new RuntimeException("Error while retrieving checkoutPage URL from response");
    }
}
