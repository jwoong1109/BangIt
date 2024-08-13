package com.bangIt.blended.service.partner.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bangIt.blended.common.util.FileUploadUtil;
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
	private final FileUploadUtil fileUploadUtil;
	
	@Override
    @Transactional
    public void saveProcess(PlaceSaveDTO dto) {
		
		// Place 엔티티 저장
        PlaceEntity placeEntity = convertToEntity(dto);
        repository.save(placeEntity);
    }
	
	@Override
	public Map<String, String> s3TempUpload(MultipartFile file) throws IOException {
		return fileUploadUtil.s3TempUpload(file);
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
