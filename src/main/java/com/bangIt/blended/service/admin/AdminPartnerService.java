package com.bangIt.blended.service.admin;

import java.util.List;

import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.enums.PlaceStatus;

import org.springframework.ui.Model;

public interface AdminPartnerService {
	
	List<PlaceEntity> retrieveAllPlaces();

	void retrievePendingApprovalPlaces(Model model);

	void addPlacesToModel(Model model);

	/**
	 * 주어진 ID에 해당하는 PlaceEntity를 조회합니다.
	 * 
	 * @param id 조회할 Place의 ID
	 * @return 조회된 PlaceEntity 객체, 없을 경우 null
	 */
	void getPlaceById(Long id, Model model);


	void approvedProcess(Long id, PlaceStatus placeStatus);
}
