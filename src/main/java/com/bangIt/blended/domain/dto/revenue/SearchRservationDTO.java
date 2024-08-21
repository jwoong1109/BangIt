package com.bangIt.blended.domain.dto.revenue;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
public class SearchRservationDTO {
	
	private LocalDate startDate;
	private LocalDate endDate;
	private String userEmail;

}
