package com.bangIt.blended.service.user.impl;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.repository.PlaceEntityRepository;
import com.bangIt.blended.service.user.UserPlaceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserPlaceServiceProcess implements UserPlaceService{
	
	private final PlaceEntityRepository repository;

	@Override
	public void detailProcess(long no, Model model) {

		// 공지사항 조회
        PlaceEntity place = repository.findById(no).orElseThrow(() -> new RuntimeException("Notice not found with id: " + no));
        model.addAttribute("place", place.toPlaceDetailDTO());
//        // 작성자인지 여부 확인
//        EmployeesEntity noticeCreator = notice.getEmployee();
//        boolean isCreator = noticeCreator != null && noticeCreator.getName().equals(currentUserName);
//
//        // 모델에 데이터 추가
		
	}
}
