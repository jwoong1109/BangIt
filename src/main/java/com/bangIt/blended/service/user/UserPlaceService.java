package com.bangIt.blended.service.user;

import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.place.PlaceDetailDTO;

public interface UserPlaceService {

	void detailProcess(long no, Model model);

}
