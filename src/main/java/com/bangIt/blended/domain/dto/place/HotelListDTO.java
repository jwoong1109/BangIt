package com.bangIt.blended.domain.dto.place;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotelListDTO {

	private Long id;
	private String name;
	private String description;
	private LocalDateTime createdAt;
	private String imageUrl; // 대표 이미지 URL 추가
	private long price;
	private double distance;
	private double latitude;
	private double longitude;
	
}
