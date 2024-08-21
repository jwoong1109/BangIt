package com.bangIt.blended.service.admin;

import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.user.admin.GetUserDtailDTO;

public interface AdminUserService {

	void getUserList(String username, Model model);

	GetUserDtailDTO getUserDetail(Long userId);

}
