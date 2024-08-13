package com.bangIt.blended.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethod {
	CARD("카드"),
	TOSS_PAY("토스페이"), 
	BANK_TRANSFER("계좌이체");
	
	private final String paymentMethodName;
	
	public final String getpaymentMethodName() {
		return paymentMethodName;
	}
}