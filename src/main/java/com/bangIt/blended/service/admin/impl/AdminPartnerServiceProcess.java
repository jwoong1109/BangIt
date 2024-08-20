package com.bangIt.blended.service.admin.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.enums.PlaceStatus;
import com.bangIt.blended.domain.repository.PlaceEntityRepository;
import com.bangIt.blended.service.admin.AdminPartnerService;
import lombok.RequiredArgsConstructor;

/**
 * AdminPartnerServiceProcess는 AdminPartnerService 인터페이스를 구현하며, 
 * 숙소 데이터와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
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
    @Override
    public List<PlaceEntity> retrievePendingApprovalPlaces() {
        // PlaceEntityRepository를 사용하여 승인 대기 중인 숙소 데이터를 조회합니다.
        return placeEntityRepository.findByStatus(PlaceStatus.PENDING_APPROVAL);
    }

    /**
     * 승인 대기 중인 숙소를 조회하여 모델에 추가합니다.
     * 
     * @param model 데이터를 추가할 모델 객체
     */
    @Override
    public void addPlacesToModel(Model model) {
        // 승인 대기 중인 숙소 목록을 조회합니다.
        List<PlaceEntity> pendingPlaces = retrievePendingApprovalPlaces();
        // 조회한 목록을 모델에 추가하여 Thymeleaf 템플릿에서 사용할 수 있도록 합니다.
        model.addAttribute("places", pendingPlaces);
    }
}
