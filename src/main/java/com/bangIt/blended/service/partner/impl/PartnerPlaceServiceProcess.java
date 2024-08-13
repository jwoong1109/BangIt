package com.bangIt.blended.service.partner.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bangIt.blended.domain.dto.place.PlaceSaveDTO;
import com.bangIt.blended.domain.entity.ImageEntity;
import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.repository.PlaceEntityRepository;
import com.bangIt.blended.domain.repository.PlaceImageRepository;
import com.bangIt.blended.service.partner.PartnerPlaceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PartnerPlaceServiceProcess implements PartnerPlaceService{
	
	//private final EmployeesEntityRepository  employeesEntityRep;
	private final PlaceEntityRepository repository;
	private final PlaceImageRepository placeImageRepository;
	
	@Override
    @Transactional
    public void saveProcess(PlaceSaveDTO dto) {
		
		// Place 엔티티 저장
        PlaceEntity placeEntity = convertToEntity(dto);
        repository.save(placeEntity);
       
        // 메인 이미지 저장
        if (dto.getMainImageUrl() != null) {
            ImageEntity mainImage = ImageEntity.builder()
            		.place(placeEntity)
                    .imageUrl(dto.getMainImageUrl())
                    .imageType(ImageEntity.ImageType.PLACE_MAIN)
                    .build();
            placeImageRepository.save(mainImage);
        }
        
        // 추가 이미지들 저장
        if (dto.getAdditionalImageUrls() != null && !dto.getAdditionalImageUrls().isEmpty()) {
            for (String imageUrl : dto.getAdditionalImageUrls()) {
                ImageEntity additionalImage = ImageEntity.builder()
                        .place(placeEntity)
                        .imageUrl(imageUrl)
                        .imageType(ImageEntity.ImageType.PLACE_ADDITIONAL)
                        .build();
                placeImageRepository.save(additionalImage);
            }
        }
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
