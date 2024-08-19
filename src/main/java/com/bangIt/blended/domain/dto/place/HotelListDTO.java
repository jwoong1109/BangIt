package com.bangIt.blended.domain.dto.place;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotelListDTO {

	private Long id;
	private String name;
	private String description;
	private String imageUrl; // 대표 이미지 URL 추가
	private long price;
	private double distance;  // 추가: 거리 필드

}
