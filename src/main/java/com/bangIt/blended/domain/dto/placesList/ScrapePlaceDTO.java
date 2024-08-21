package com.bangIt.blended.domain.dto.placesList;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ScrapePlaceDTO {
    private String name;
    private String grade;
    private String imageUrl;
    private String location;
    private String price;
    private String url;


}
