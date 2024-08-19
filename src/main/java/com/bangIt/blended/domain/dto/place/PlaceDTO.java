package com.bangIt.blended.domain.dto.place;

import java.util.List;

import com.bangIt.blended.domain.enums.PlaceType;
import com.bangIt.blended.domain.enums.Region;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PlaceDTO {

	private Long id;
	private String name;
	private String description;
	private String detailedAddress;
	private Double latitude;
	private Double longitude;
	private Region region;
	private PlaceType type;
	private List<String> themes;
	private int lowestPrice; // 가장 낮은 방 가격
    private String imageUrl; // 대표 이미지 URL 추가
	
	
	   // 편의 메소드: 주소 전체 반환
    public String getFullAddress() {
        return region.getKoName() + " " + detailedAddress;
    }
	

}
