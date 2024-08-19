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

		// 상세페이지 조회
        PlaceEntity place = repository.findById(no).orElseThrow(() -> new RuntimeException("Notice not found with id: " + no));     
        
        model.addAttribute("place", place.toPlaceDetailDTO());
		
	}
}
