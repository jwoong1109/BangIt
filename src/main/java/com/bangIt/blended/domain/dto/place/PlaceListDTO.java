package com.bangIt.blended.domain.dto.place;

import java.time.LocalDateTime;

import com.bangIt.blended.domain.enums.PlaceStatus;
import com.bangIt.blended.domain.enums.PlaceType;
import com.bangIt.blended.domain.enums.Region;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlaceListDTO {

		private long id;
		private Region region;
		private String name;
		private PlaceType type;
		private LocalDateTime updatedAt;
		private PlaceStatus status;

}
