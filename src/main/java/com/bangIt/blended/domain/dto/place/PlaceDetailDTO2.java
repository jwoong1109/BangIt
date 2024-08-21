package com.bangIt.blended.domain.dto.place;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.bangIt.blended.domain.dto.ImageListDTO;
import com.bangIt.blended.domain.enums.PlaceStatus;
import com.bangIt.blended.domain.enums.PlaceTheme;
import com.bangIt.blended.domain.enums.PlaceType;
import com.bangIt.blended.domain.enums.Region;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlaceDetailDTO2 {
		
		private long id;
		private Region region;
		private String name;
		private String description;
		private String detailedAddress;
		private PlaceType type;
		private LocalDateTime updatedAt;
		private PlaceStatus status;
		
		private Set<PlaceTheme> themes;
		
		private List<ImageListDTO> images;

}
