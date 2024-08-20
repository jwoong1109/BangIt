package com.bangIt.blended.domain.dto;

import com.bangIt.blended.domain.entity.ImageEntity.ImageType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageListDTO {
	private Long id;
	@Builder.Default
	private String baseUrl = "https://s3.ap-northeast-2.amazonaws.com/nowon.images.host0521/";
    private String imageUrl;

    private ImageType imageType;
}
