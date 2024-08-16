package com.bangIt.blended.service.admin;

import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.naverWorks.WritMailDTO;

public interface AdminMailService {

	void getMailFolderProcess(Model model, String accessToken);

	void getMailFolderListProcess(String folderId, Model model, String accessToken);

	void getMailDetail(String mailId, Model model, String accessToken);

	void WritMailProcess(WritMailDTO dto, String accessToken);



}
