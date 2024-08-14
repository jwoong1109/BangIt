package com.bangIt.blended.service.admin.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.common.util.NaverWorksUtil;
import com.bangIt.blended.domain.dto.naverWorks.GetEmployeeDTO;
import com.bangIt.blended.service.admin.AdminEmployeeService;
import com.fasterxml.jackson.databind.JsonNode;


import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class AdminEmployeeServicePocess implements AdminEmployeeService {

	private final NaverWorksUtil naverWorksUtil;

	
	
	//구성원 정보 가져오기(세션에 저장해둔 억세스 토큰과 모델을 가져옴)
	@Override
	public void getEmployListProcess(Model model, String accessToken) {
        //예외 처리를 통해 응답이 성공 했을때만 json으로 받은걸 JsonNode로 데이터를 표현함(JsonNode는 Jackson 라이브러리에서 JSON 데이터를 표현하는 클래스)
		try {
			//유틸에 정의해둔 get메서드에 억세스 토큰과, 요청 url의 엔드포인트, 
			ResponseEntity<JsonNode> response = naverWorksUtil.get(accessToken, "/users");
			 JsonNode responseBody = response.getBody();
	         System.out.println("API Response: " + responseBody); // 디버깅용
		
			if (response.getStatusCode() == HttpStatus.OK) {
				
				model.addAttribute("employees", StreamSupport.stream(response.getBody().path("users").spliterator(), false).map(GetEmployeeDTO::toDTO).collect(Collectors.toList()));
			} else {
				model.addAttribute("errorMessage", "Failed to fetch employee list. Status code: " + response.getStatusCode());
			}
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error fetching employee list: " + e.getMessage());
		}

	}


}
