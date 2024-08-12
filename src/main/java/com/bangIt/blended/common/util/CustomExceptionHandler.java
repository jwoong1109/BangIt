package com.bangIt.blended.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bangIt.blended.common.util.exception.CustomApiException;

@RestControllerAdvice
public class CustomExceptionHandler {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e){
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, "런타임오류", null), HttpStatus.BAD_REQUEST);
		
	}

}
