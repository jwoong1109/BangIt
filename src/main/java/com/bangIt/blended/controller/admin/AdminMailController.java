package com.bangIt.blended.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bangIt.blended.service.admin.AdminEmployeeService;
import com.bangIt.blended.service.admin.AdminMailService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AdminMailController {

	private final AdminMailService adminMailService;

	@GetMapping("/admin/mail")
	public String mailList(Model model, HttpSession session) {

		String accessToken = (String) session.getAttribute("accessToken");
		if (accessToken == null) {
			return "redirect:/admin"; // 토큰이 없으면 다시 인증
		}

		adminMailService.getMailFolderProcess(model, accessToken);
		
		
		
		return "/views/admin/mail/list";

	}

}
