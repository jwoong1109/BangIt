package com.bangIt.blended.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PlaceTheme {
	
	BREAKFAST(1, "조식포함"),
    SPA(2,"스파"),
    PET_FRIENDLY(3, "반려견동반"),
    SMOKING(4, "객실내흡연"),
    COOKING(5, "객실내취사"),
    COUPLE_PC(6, "커플PC"),
    FREE_PARKING(7, "무료주차"),
    PICKUP(8, "픽업가능");
	
	private final int number;
	private final String KoName;

}
