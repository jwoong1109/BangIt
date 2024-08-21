package com.bangIt.blended.domain.dto.place;

import com.bangIt.blended.domain.enums.PlaceStatus;

import lombok.Data;


@Data
public class ApprovOrRejectDTO {
	private PlaceStatus placeStatus;
}
