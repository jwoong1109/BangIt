package com.bangIt.blended.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthProvider {

	 GOOGLE("Google"),
	 NAVER("Naver"),
	 KAKAO("Kakao")
	 ;
	
	private final String providerName;
	
	public String providerName() {
        return providerName;
    }
}
