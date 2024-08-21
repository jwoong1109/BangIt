package com.bangIt.blended.domain.dto.user.admin;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class getUserDTO {
	
    private Long id;
    private String email;
    private String username;
    private boolean isActive;
    private String provider;

}
