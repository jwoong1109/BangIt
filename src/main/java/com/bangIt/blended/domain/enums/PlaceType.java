package com.bangIt.blended.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PlaceType {
	
	HOTEL(1, "호텔"),
    MOTEL(2, "모텔"),
    PENSION(3, "펜션"),
    RESORT(4, "리조트"),
    CAMPING(5, "캠핑/글램핑"),
    GUESTHOUSE(6, "게스트하우스");
	
	private final int number;
	private final String KoName;

}
