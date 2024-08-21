package com.bangIt.blended.service.admin.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.user.admin.GetActivityLogDTO;
import com.bangIt.blended.domain.dto.user.admin.GetReservationDTO;
import com.bangIt.blended.domain.dto.user.admin.GetUserDtailDTO;
import com.bangIt.blended.domain.mapper.UserMapper;
import com.bangIt.blended.service.admin.AdminUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminUserServiceProcess implements AdminUserService {
	
	
	private final UserMapper userMapper;
	
	
	//초기 화면과 사용자 검색
	@Override
	public void getUserList(String username, Model model) {
		
		if(username == null) {
			
			model.addAttribute("user", userMapper.getAllUsers());
			
		}else {
			
			model.addAttribute("user", userMapper.searchUsers(username));
			
		}
		
		model.addAttribute("username", username);
		
		
	}
    
	
	//특정 사용자의 활동 기록
	@Override
	public GetUserDtailDTO getUserDetail(Long userId) {
		
		
		List<GetReservationDTO> reservations= userMapper.getUserReservations(userId);
		List<GetActivityLogDTO> activities= userMapper.getUserActivityLogs(userId);
		
		
		return GetUserDtailDTO.builder().activities(activities).reservations(reservations).build();
	}

}
