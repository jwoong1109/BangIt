package com.bangIt.blended.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActivityType {
	
    SEARCH_ACCOMMODATION(1,"숙소 검색"),
    BOOK_ACCOMMODATION(2,"숙소 예약"),
    CANCEL_RESERVATION(3,"예약 취소"),
    WRITE_REVIEW(4,"리뷰 작성"),
    LOGIN(5,"로그인"),
    LOGOUT(6,"로그아웃"),
	CHOOSING_ACCOMMODATION(7,"숙소선택");
	
	
	
	
	
	private final int number;
	private final String KoName;
	
	public String getDescription() {
	    return this.KoName;
	}

}
