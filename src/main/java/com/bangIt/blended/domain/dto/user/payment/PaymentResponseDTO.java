package com.bangIt.blended.domain.dto.user.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponseDTO {
	
	private Long paymentId; // 결제 ID
	private String paymentStatus; // 결제 상태 (예: COMPLETED, PENDING, FAILED)
	private BigDecimal amount; // 결제 금액
	private LocalDateTime paymentDate; // 결제 일자
	private String paymentMethod; // 결제 방법
	private String responseMessage; // API 응답 메시지
}
