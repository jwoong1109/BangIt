package com.bangIt.blended.controller.user.payment;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bangIt.blended.service.user.PaymentService;
import com.bangIt.blended.domain.enums.PaymentMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
@RequiredArgsConstructor  // final 필드에 대해 생성자를 자동으로 생성해 주입하는 Lombok 애너테이션
public class WidgetController {

    private final PaymentService paymentService;  // 결제 관련 비즈니스 로직을 처리하는 서비스 주입
    private final Logger logger = LoggerFactory.getLogger(WidgetController.class);

    //POST요청을 처리하여 클라이언트에서 받은 결제 정보를 바탕으로 토스페이먼츠 api에 결제 승인 요청을 합니다
    @RequestMapping(value = "/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {

        logger.info("confirmPayment 메서드 호출됨");

        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;
        String reservationId;
        String paymentMethod;

        try {
            // 클라이언트에서 전송된 JSON 데이터를 파싱하여 필요한 결제 정보 추출
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = String.valueOf(requestData.get("orderId")); // orderId를 String으로 변환
            amount = String.valueOf(requestData.get("amount"));   // amount도 String으로 변환
            reservationId = String.valueOf(requestData.get("reservationId")); // 예약 ID를 추출하여 String으로 변환
            paymentMethod = (String) requestData.get("paymentMethod"); // 결제 방법을 추출
            logger.info("요청 데이터 파싱 완료 - paymentKey: {}, orderId: {}, amount: {}, reservationId: {}, paymentMethod: {}",
                paymentKey, orderId, amount, reservationId, paymentMethod);
        } catch (ParseException e) {
            logger.error("JSON 파싱 오류: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        logger.info("토스페이 API 요청 준비 완료");

        // 토스페이먼츠 API에 요청을 보낼 때 필요한 인증 헤더 설정
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";  // 여기에 실제 시크릿 키를 사용해야 합니다.
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제 승인 요청을 위한 HTTP 연결 설정
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // 요청 데이터를 API 서버로 전송
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));
        logger.info("토스페이 API 요청 전송");

        // API 서버로부터의 응답을 수신
        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;  // HTTP 상태 코드가 200이면 요청이 성공한 것으로 간주
        logger.info("토스페이 API 응답 코드: {}", code);

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        // API 응답 데이터를 파싱
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        logger.info("토스페이 API 응답 파싱 완료");

        if (isSuccess) {
            // 결제가 성공했을 때 처리할 비즈니스 로직
            Long reservationIdValue = Long.parseLong(reservationId);  // String으로 받은 reservationId를 Long으로 변환
            Long amountValue = Long.parseLong(amount);  // String으로 받은 amount를 Long으로 변환
            PaymentMethod paymentMethodEnum = PaymentMethod.valueOf(paymentMethod.toUpperCase()); // String을 ENUM으로 변환
            logger.info("결제 성공 - DB에 결제 정보 저장 시작");
            paymentService.savePaymentInfo(paymentKey, orderId, amountValue, reservationIdValue, paymentMethodEnum);  // 결제 정보를 DB에 저장
            logger.info("DB에 결제 정보 저장 완료");
        }

        // 최종적으로 API 서버로부터 받은 응답 데이터를 클라이언트로 반환
        return ResponseEntity.status(code).body(jsonObject);
    }
}
