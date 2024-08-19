package com.bangIt.blended.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    PENDING("결제 대기"),
    COMPLETED("결제 완료"),
    FAILED("결제 실패"),
    CANCELLED("결제 취소");

	private final String PaymentStatusName;
	
	 public final String PaymentStatusName() {
	        return PaymentStatusName;
	    }
	
}
