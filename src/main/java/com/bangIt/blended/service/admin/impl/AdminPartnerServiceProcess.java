package com.bangIt.blended.service.admin.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.place.PlaceListDTO;
import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.enums.PlaceStatus;
import com.bangIt.blended.domain.repository.PlaceEntityRepository;
import com.bangIt.blended.service.admin.AdminPartnerService;

import lombok.RequiredArgsConstructor;

/**
 * AdminPartnerServiceProcess는 AdminPartnerService 인터페이스를 구현하며, 숙소 데이터와 관련된 비즈니스
 * 로직을 처리하는 서비스 클래스입니다.
 */

@Service
@RequiredArgsConstructor
public class AdminPartnerServiceProcess implements AdminPartnerService {

	// PlaceEntityRepository를 통해 데이터베이스와 상호작용합니다.
	private final PlaceEntityRepository placeEntityRepository;

	/**
	 * 모든 PlaceEntity 객체를 조회합니다.
	 * 
	 * @return 모든 PlaceEntity의 리스트
	 */
	@Override
	public List<PlaceEntity> retrieveAllPlaces() {
		// PlaceEntityRepository를 사용하여 모든 숙소 데이터를 조회합니다.
		return placeEntityRepository.findAll();
	}

	/**
	 * 상태가 PENDING_APPROVAL인 PlaceEntity 객체만 조회합니다.
	 * 
	 * @return 승인 대기 중인 PlaceEntity의 리스트
	 */
	//*
	@Override
	public void retrievePendingApprovalPlaces(Model model) {
		// PlaceEntityRepository를 사용하여 승인 대기 중인 숙소 데이터를 조회합니다.
		List<PlaceListDTO> pendingPlaces = placeEntityRepository.findByStatus(PlaceStatus.PENDING_APPROVAL)
				.stream()//List<PlaceEntity> -> Stream<PlaceEntity>
				.map(PlaceEntity::toPlaceListDTO)// Stream<PlaceEntity> -> Stream<PlaceListDTO>
				.collect(Collectors.toList()); //List<PlaceListDTO>
		// 조회한 목록을 모델에 추가하여 Thymeleaf 템플릿에서 사용할 수 있도록 합니다.
		model.addAttribute("places", pendingPlaces);
	}
	//*/

	/**
	 * 승인 대기 중인 숙소를 조회하여 모델에 추가합니다.
	 * 
	 * @param model 데이터를 추가할 모델 객체
	 */
	@Override
	public void addPlacesToModel(Model model) {
		// 승인 대기 중인 숙소 목록을 조회합니다.
		List<PlaceListDTO> pendingPlaces = placeEntityRepository.findByStatus(PlaceStatus.PENDING_APPROVAL)
				.stream()//List<PlaceEntity> -> Stream<PlaceEntity>
				.map(PlaceEntity::toPlaceListDTO)// Stream<PlaceEntity> -> Stream<PlaceListDTO>
				.collect(Collectors.toList()); //List<PlaceListDTO>
		// 조회한 목록을 모델에 추가하여 Thymeleaf 템플릿에서 사용할 수 있도록 합니다.
		model.addAttribute("places", pendingPlaces);
	}

	/**
	 * 주어진 ID에 해당하는 PlaceEntity를 조회합니다.
	 * 
	 * @param id 조회할 Place의 ID
	 * @return 조회된 PlaceEntity 객체, 없을 경우 null
	 */
	@Override
	public void getPlaceById(Long id, Model model) {
		
		model.addAttribute("place", placeEntityRepository.findById(id)
				.map(PlaceEntity::toPlaceDetailDTO2)
				.orElseThrow());
		
	}

	// 추가: 승인 메서드,// 추가: 거절 메서드
	@Override
	@Transactional
	public void approvedProcess(Long id, PlaceStatus placeStatus) {
		System.out.println("<<<<<placeStatus:"+placeStatus);
		placeEntityRepository.findById(id)
		//.map(entity->entity.status(PlaceStatus.APPROVED))
		.orElseThrow().status(placeStatus);
		
	}

	
	
	
}
