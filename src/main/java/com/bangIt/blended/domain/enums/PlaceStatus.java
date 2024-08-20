package com.bangIt.blended.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PlaceStatus {

    PENDING_APPROVAL(1, "승인대기중"),
    APPROVED(2, "승인완료"),
    WITHDRAWN(3, "탈퇴"), 
    REJECTED(4, "거절됨");
	
	private final int number;
	private final String koName;

}
