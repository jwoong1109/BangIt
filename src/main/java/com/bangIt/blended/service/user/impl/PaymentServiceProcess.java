package com.bangIt.blended.service.user.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.bangIt.blended.common.config.TossPaymentConfig;
import com.bangIt.blended.domain.dto.user.payment.PaymentRequestDTO;
import com.bangIt.blended.domain.entity.PaymentEntity;
import com.bangIt.blended.service.user.PaymentService;
import com.bangIt.blended.domain.repository.PaymentEntityRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PaymentServiceProcess implements PaymentService {
	private final TossPaymentConfig tossPaymentConfig;
	private final RestTemplate restTemplate;
	private final PaymentEntityRepository paymentRepository;

	@Override
	public String createPayment(PaymentRequestDTO paymentRequestDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Basic "
				+ Base64.getEncoder().encodeToString((tossPaymentConfig.getTestSecretApiKey() + ":").getBytes()));

		JSONObject requestBody = new JSONObject();
		requestBody.put("orderNo", paymentRequestDTO.getOrderNo());
		requestBody.put("amount", paymentRequestDTO.getAmount());
		requestBody.put("amountTaxFree", 0); // 필요에 따라 추가
		requestBody.put("productDesc", paymentRequestDTO.getProductDesc());
		requestBody.put("apiKey", "sk_test_w5lNQylNqa5lNQe013Nq");
		requestBody.put("autoExecute", true); // 필요에 따라 추가
		requestBody.put("resultCallback", paymentRequestDTO.getResultCallback()); // 필요에 따라 추가
		requestBody.put("retUrl", paymentRequestDTO.getRetUrl());
		requestBody.put("retCancelUrl", paymentRequestDTO.getRetCancelUrl());

		HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

		ResponseEntity<String> response = restTemplate.exchange("https://pay.toss.im/api/v2/payments", HttpMethod.POST,
				entity, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			JSONObject jsonResponse = new JSONObject(response.getBody());
			return jsonResponse.getString("checkoutPage");
		} else {
			// 에러 처리 개선
			return "결제 생성 중 오류 발생";
		}
	}

	@Override
	public boolean validatePayment(String paymentKey, String orderId, Long amount) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Basic "
				+ Base64.getEncoder().encodeToString((tossPaymentConfig.getTestSecretApiKey() + ":").getBytes()));

		JSONObject requestBody = new JSONObject();
		requestBody.put("orderId", orderId);
		requestBody.put("amount", amount);

		HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

		ResponseEntity<String> response = restTemplate.exchange(
				"https://api.tosspayments.com/v1/payments/" + paymentKey, HttpMethod.POST, entity, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			JSONObject jsonResponse = new JSONObject(response.getBody());
			return "DONE".equals(jsonResponse.getString("status"));
		}

		return false;
	}

	@Override
	public void savePaymentInfo(String paymentKey, String orderId, Long amount) {
		// TODO Auto-generated method stub
		
	}
	/*
	 * @Override public void savePaymentInfo(String paymentKey, String orderId, Long
	 * amount) { PaymentEntity payment =
	 * PaymentEntity.builder().paymentKey(paymentKey).orderId(orderId).amount(
	 * amount) .status("DONE").build();
	 * 
	 * paymentRepository.save(payment); }
	 */
}