package com.bangIt.blended.service.admin.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.common.util.NaverWorksUtil;
import com.bangIt.blended.service.admin.AdminEmployeeService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Service
public class AdminEmployeeServicePocess implements AdminEmployeeService {

	private final NaverWorksUtil naverWorksUtil;

	@Override
	public void getEmployListProcess(Model model, String accessToken) {

		try {
			ResponseEntity<EmployeeListResponse> response = naverWorksUtil.get(accessToken, "/users",
					EmployeeListResponse.class);
			System.out.println("API Response: " + response.getBody());

			if (response.getStatusCode() == HttpStatus.OK) {
				model.addAttribute("employees", response.getBody().getUsers());
			} else {
				model.addAttribute("error", "Failed to fetch employee list. Status code: " + response.getStatusCode());
			}
		} catch (Exception e) {
			model.addAttribute("error", "Error fetching employee list: " + e.getMessage());
		}

	}

	@ToString
	@Getter
	@Setter
	private static class EmployeeListResponse {
		private List<UserResponse> users;
		private Object responseMetaData;
	}

	@ToString
	@Getter
	@Setter
	public static class UserResponse {
		private String userId;
		private String email;
		private Object userName;
		private String cellPhone;
		private String hiredDate;
		
	}

}
