package com.bangIt.blended.service.partner.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.bangIt.blended.common.util.FileUploadUtil;
import com.bangIt.blended.domain.dto.room.RoomListDTO;
import com.bangIt.blended.domain.dto.room.RoomSaveDTO;
import com.bangIt.blended.domain.entity.ImageEntity;
import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.entity.RoomEntity;
import com.bangIt.blended.domain.enums.PlaceStatus;
import com.bangIt.blended.domain.repository.ImageEntityRepository;
import com.bangIt.blended.domain.repository.PlaceEntityRepository;
import com.bangIt.blended.domain.repository.RoomEntityRepository;
import com.bangIt.blended.service.partner.PartnerRoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PartnerRoomServiceProcess implements PartnerRoomService{

	private final RoomEntityRepository roomRepository;
    private final PlaceEntityRepository placeRepository;

    private final FileUploadUtil fileUploadUtil;
    private final ImageEntityRepository imageRepository; 

    @Override
    @Transactional
    public void saveRoom(RoomSaveDTO dto) {
        // 메인 이미지 처리
        if (dto.getMainImageBucketKey() != null && !dto.getMainImageBucketKey().isEmpty()) {
            List<String> mainImageKeys = new ArrayList<>();
            mainImageKeys.add(dto.getMainImageBucketKey());
            List<String> mainImageUrls = fileUploadUtil.s3TempToImages(mainImageKeys);
            if (!mainImageUrls.isEmpty()) {
                dto.setMainImageBucketKey(mainImageUrls.get(0));
            }
        }

        // 추가 이미지 처리
        if (dto.getAdditionalImageBucketKeys() != null && !dto.getAdditionalImageBucketKeys().isEmpty()) {
            List<String> additionalImageUrls = fileUploadUtil.s3TempToImages(dto.getAdditionalImageBucketKeys());
            dto.setAdditionalImageBucketKeys(additionalImageUrls);
        }

        PlaceEntity placeEntity = placeRepository.findById(dto.getPlaceId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid Place ID"));

        RoomEntity roomEntity = toRoomEntity(dto, placeEntity);
        roomRepository.save(roomEntity);

        // 이미지 정보 저장
        saveImages(roomEntity, dto);
    }

    private void saveImages(RoomEntity room, RoomSaveDTO dto) {
        // 메인 이미지 저장
        if (dto.getMainImageBucketKey() != null && !dto.getMainImageBucketKey().isEmpty()) {
            ImageEntity mainImage = ImageEntity.builder()
                .room(room)
                .imageUrl(dto.getMainImageBucketKey())
                .imageType(ImageEntity.ImageType.ROOM_MAIN)
                .build();
            imageRepository.save(mainImage);
        }
        
        // 추가 이미지 저장
        if (dto.getAdditionalImageBucketKeys() != null && !dto.getAdditionalImageBucketKeys().isEmpty()) {
            List<ImageEntity> additionalImages = new ArrayList<>();
            for (String url : dto.getAdditionalImageBucketKeys()) {
                ImageEntity additionalImage = ImageEntity.builder()
                    .room(room)
                    .imageUrl(url)
                    .imageType(ImageEntity.ImageType.ROOM_ADDITIONAL)
                    .build();
                additionalImages.add(additionalImage);
            }
            imageRepository.saveAll(additionalImages);
        }
    }

    //방 등록
    private RoomEntity toRoomEntity(RoomSaveDTO dto, PlaceEntity placeEntity) {
        return RoomEntity.builder()
                .place(placeEntity)
                .roomName(dto.getRoomName())
                .roomInformation(dto.getRoomInformation())
                .roomPrice(dto.getRoomPrice())
                .roomStatus(RoomEntity.RoomStatus.valueOf(dto.getRoomStatus()))
                .refundPolicy(dto.getRefundPolicy())
                .checkInTime(dto.getCheckInTime())
                .checkOutTime(dto.getCheckOutTime())
                .guests(dto.getGuests())
                .build();
    }

    private RoomListDTO toRoomListDTO(RoomEntity room) {
        return RoomListDTO.builder()
            .id(room.getId())
            .roomName(room.getRoomName())
            .roomPrice(room.getRoomPrice())
            .roomStatus(room.getRoomStatus())
            .guests(room.getGuests())
            .placeName(room.getPlace().getName())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomListDTO> listProcess(Long placeId, PlaceStatus placeStatus) {
        PlaceEntity place = placeRepository.findById(placeId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Place ID: " + placeId));
        
        if (place.getStatus() != placeStatus) {
            throw new IllegalStateException("Place status mismatch");
        }
        
        List<RoomEntity> rooms = roomRepository.findByPlace(place);
        return rooms.stream()
            .map(this::toRoomListDTO)
            .collect(Collectors.toList());
    }

	@Override
	public Map<String, String> uploadImage(MultipartFile file) throws IOException {
	    return fileUploadUtil.s3TempUpload(file);
	}

	//상세페이지 조회
	@Override
	public void detailProcess(Long id, Model model) {
		RoomEntity room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));            
		model.addAttribute("room", room.toRoomDetailDTO());
	}
	
}
