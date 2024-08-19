package com.bangIt.blended.controller.admin;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.repository.PlaceEntityRepository;
import lombok.RequiredArgsConstructor;

/**
 * AdminPartnerController는 관리자의 파트너 페이지와 관련된 요청을 처리합니다.
 */
@Controller
@RequiredArgsConstructor
public class AdminPartnerController {

    // PlaceEntity 데이터를 관리하기 위한 리포지토리 객체
    private final PlaceEntityRepository placeEntityRepository;
    
    /**
     * 관리자 기본 파트너 페이지를 렌더링합니다.
     * @return 기본 파트너 페이지의 뷰 이름
     */
    @GetMapping("/admin/partner")
    public String adminPartner() {
        // "views/admin/partner/partner" 템플릿을 반환하여 기본 파트너 페이지를 렌더링
        return "views/admin/partner/partner";
    }

    /**
     * 숙소 관리 페이지를 렌더링하고, 데이터베이스에서 모든 PlaceEntity 데이터를 가져와서 모델에 추가합니다.
     * @param model Thymeleaf 템플릿에 데이터를 전달하기 위한 모델 객체
     * @return 숙소 관리 페이지의 뷰 이름
     */
    @GetMapping("/admin/partner/placeManagement")
    public String placeManagement(Model model) {
        // 데이터베이스에서 모든 PlaceEntity를 조회
        List<PlaceEntity> places = placeEntityRepository.findAll();
        
        // 조회한 PlaceEntity 리스트를 모델에 추가하여 Thymeleaf 템플릿에 전달
        model.addAttribute("places", places);
        
        // "views/admin/partner/placeManagement" 템플릿을 반환하여 숙소 관리 페이지를 렌더링
        return "views/admin/partner/placeManagement";
    }
}
