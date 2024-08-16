package com.bangIt.blended.domain.dto.place;

import java.util.List;
import java.util.Set;

import com.bangIt.blended.domain.enums.PlaceTheme;
import com.bangIt.blended.domain.enums.PlaceType;
import com.bangIt.blended.domain.enums.Region;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlaceUpdateDTO {

	private long id;
	private String name;
    private String description;
    private Region region;
    private String detailedAddress;
    private PlaceType type;
    private Set<PlaceTheme> themes;
    private Double latitude;
    private Double longitude;
    // 새로 추가된 필드
    private String mainImage;
    private List<String> additionalImages;

}
