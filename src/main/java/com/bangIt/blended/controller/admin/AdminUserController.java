package com.bangIt.blended.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bangIt.blended.domain.dto.user.admin.GetUserDtailDTO;
import com.bangIt.blended.service.admin.AdminUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AdminUserController {
	
	private final AdminUserService  adminUserService;

	
	@GetMapping("/admin/user")
	public String adminPartner(@RequestParam(name = "username", required = false) String username, Model model) {
		
		adminUserService.getUserList(username,model);
		
		
		
		return "views/admin/user/user";
	}
	
	@ResponseBody
    @GetMapping("admin/user/detail/{userId}")
    public ResponseEntity<GetUserDtailDTO> getUserDetail0(@PathVariable(name = "userId") Long userId) {
		
		GetUserDtailDTO userDetail = adminUserService.getUserDetail(userId);
		
        return ResponseEntity.ok(userDetail);
    }

}
