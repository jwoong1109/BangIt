package com.bangIt.blended.service.admin;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface AdminDriveService {

	void getMyDriveProcess(Model model, String accessToken);

	void getMyDriveRootFileProcess(Model model, String accessToken);

	ResponseEntity<ByteArrayResource> getFildeDownloadUrl(String accessToken, String fileId);

	void deleteFilde(String accessToken, String fileId);

	void postFile(MultipartFile file, String accessToken) throws IOException;
}
