package com.bangIt.blended.domain.dto.placesList;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.bangIt.blended.domain.enums.Region;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SearchPlaceDTO {
	private Region region;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkinDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkoutDate;
    
    private int guestCount;
    
    private List<String> accommodationTypes;
    private List<String> themes;
	
}
