package com.bangIt.blended.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Region {
	
	SEOUL(1, "서울"),
    GYEONGGI(2, "경기도"),
    GANGWON(3, "강원도"),
    CHUNGCHEONG(4, "충청도"),
    GYEONGSANG(5, "경상도"),
    JEOLLA(6, "전라도"),
    JEJU(7, "제주도"),
    DOKDO(8, "독도");
	
	private final int number;
	private final String KoName;

}
