package com.bangIt.blended.domain.dto.user.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bangIt.blended.domain.entity.PaymentEntity;
import com.bangIt.blended.domain.enums.PaymentMethod;
import com.bangIt.blended.domain.enums.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentRequestDTO {

	private BigDecimal amount; // 결제 금액
	private String orderId; // 주문 ID
	private String paymentMethod; // 결제 방법 (예: CARD, TRANSFER 등)
	private String orderName; // 주문 이름/설명
	private String Prodect;
	
	
	// DTO를 PaymentEntity로 변환하는 메서드
	public PaymentEntity toEntity() {
		return PaymentEntity.builder().amount(this.amount).paymentMethod(PaymentMethod.valueOf(this.paymentMethod))
				.paymentStatus(PaymentStatus.PENDING) // 초기 상태 설정
				.paymentDate(LocalDateTime.now()) // 결제 일자 설정
				.build();
	}

}
