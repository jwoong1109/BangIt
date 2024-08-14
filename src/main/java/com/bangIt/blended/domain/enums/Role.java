package com.bangIt.blended.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
	ADMIN("관리자")
	,PARTNER("판매자")
	,USER("사용자")
	;
	
	private final String roleName;
	
	//getter메서드임
	public final String roleName() {
		return roleName;
	}
}
