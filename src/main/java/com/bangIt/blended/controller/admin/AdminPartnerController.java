package com.bangIt.blended.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bangIt.blended.domain.dto.place.ApprovOrRejectDTO;
import com.bangIt.blended.service.admin.AdminPartnerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AdminPartnerController는 관리자의 파트너 페이지와 관련된 요청을 처리합니다.
 */

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminPartnerController {

    // PlaceEntity 데이터를 관리하기 위한 리포지토리 객체
    private final AdminPartnerService adminPartnerService;
    
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
        // PENDING_APPROVAL 상태의 PlaceEntity만 조회합니다.
    	adminPartnerService.retrievePendingApprovalPlaces(model);
        return "views/admin/partner/placeManagement";
    }
    
    /**
     * 숙소 상세 정보를 조회하고 해당 정보를 뷰에 전달합니다.
     * 
     * @param id 조회할 숙소의 ID
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 뷰 이름
     */
    @GetMapping("/admin/partner/placeManagementDetails/{id}")
    public String placeManagementDetails(@PathVariable("id") Long id, Model model) {
        log.info("Requested place details for ID: {}", id);

        try {
            adminPartnerService.getPlaceById(id, model);
            
            return "views/admin/partner/placeManagementDetails";
        } catch (Exception e) {
            log.error("Error occurred while fetching place details for ID: {}", id, e);
            return "redirect:/admin/partner/placeManagement";
        }
    }
    
    @ResponseBody
    @PutMapping("/admin/partner/placeManagementDetails/{id}")
    // @RequestBody ApprovOrRejectDTO dto -> @RequestParam PlaceStatus placeStatus로 enum 객체로 받을 수도 있다
    public ResponseEntity<Void> placeManagementApprovOrReject(@PathVariable("id") Long id,@RequestBody ApprovOrRejectDTO dto) {
    	//System.out.println(">>>>dto:"+dto);
    	// enum 객체로 받았을 때
    	// adminPartnerService.approvedProcess(id, placeStatus);
    	adminPartnerService.approvedProcess(id, dto.getPlaceStatus());
    	return ResponseEntity.ok().build();
    }

}
