package com.bangIt.blended.service.admin.impl;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.common.util.NaverWorksUtil;
import com.bangIt.blended.domain.dto.naverWorks.GetMailFolderDTO;
import com.bangIt.blended.service.admin.AdminEmployeeService;
import com.bangIt.blended.service.admin.AdminMailService;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminMailServiceProcess implements AdminMailService {

	private final NaverWorksUtil naverWorksUtil;

	// 메일함 정보 가져오기
	@Override
	public void getMailFolderProcess(Model model, String accessToken) {

		try {

			ResponseEntity<JsonNode> response = naverWorksUtil.get(accessToken, "/users/me/mail/mailfolders");
			 JsonNode responseBody = response.getBody();
	         System.out.println("API Response: " + responseBody);
	         
	        if(response.getStatusCode() == HttpStatus.OK) {
	        	model.addAttribute("mailFolder", StreamSupport.stream(response.getBody().path("mailFolders").spliterator(),false).map(GetMailFolderDTO::toDTO).collect(Collectors.toList()));
	        	
	        }

		} catch (Exception e) {
			model.addAttribute("errorMessage", "Error fetching Mail list: " + e.getMessage());
		}

	}

	
	
	
	
	
	
	
	
}
