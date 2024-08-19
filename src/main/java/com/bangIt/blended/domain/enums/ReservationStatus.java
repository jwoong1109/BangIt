package com.bangIt.blended.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationStatus {
	PENDING("대기중"), // 대기 중
	CONFIRMED("예약확정"), // 확정됨
	CANCELLED("예약취소"); // 취소됨

	private final String ReservationStatusName;

	public final String ReservationStatusName() {
		return ReservationStatusName;
	}
}
