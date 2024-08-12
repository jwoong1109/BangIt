package com.bangIt.blended.service.partner.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bangIt.blended.domain.dto.place.PlaceSaveDTO;
import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.repository.PlaceEntityRepository;
import com.bangIt.blended.service.partner.PartnerPlaceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PartnerPlaceServiceProcess implements PartnerPlaceService{
	
	//private final EmployeesEntityRepository  employeesEntityRep;
	private final PlaceEntityRepository repository;
	
	@Override
    @Transactional
    public void saveProcess(PlaceSaveDTO dto) {
        repository.save(convertToEntity(dto));
    }

    private PlaceEntity convertToEntity(PlaceSaveDTO dto) {
        return PlaceEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .region(dto.getRegion())
                .detailedAddress(dto.getDetailedAddress())
                .type(dto.getType())
                .themes(dto.getThemes())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }
}
