package com.bangIt.blended.service.admin;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface AdminLoginService {

	void getTokenProcess(HttpServletResponse response) throws IOException;


	void getAccessTokenProcess(String code, String state, HttpSession session);

}
