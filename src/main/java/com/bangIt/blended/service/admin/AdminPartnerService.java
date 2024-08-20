package com.bangIt.blended.service.admin;

import java.util.List;

import com.bangIt.blended.domain.entity.PlaceEntity;
import org.springframework.ui.Model;

public interface AdminPartnerService {
	
	List<PlaceEntity> retrieveAllPlaces();

	List<PlaceEntity> retrievePendingApprovalPlaces();

	void addPlacesToModel(Model model);
}
