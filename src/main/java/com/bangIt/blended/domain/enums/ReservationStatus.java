package com.bangIt.blended.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
	PENDING(1, "대기중"), // 대기 중
	CONFIRMED(2, "예약확정"), // 확정됨
	CANCELLED(3, "예약취소"); // 취소됨

	private final int number;
	private final String koName;
	
}
