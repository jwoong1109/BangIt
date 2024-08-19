package com.bangIt.blended.service;

import java.util.List;

import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.place.HotelListDTO;

import jakarta.servlet.http.HttpServletRequest;



public interface IndexService {
	
	List<HotelListDTO> TopPriceHotelList(Model model);

	List<HotelListDTO> getNearbyHotels(String ipAddress);

	List<HotelListDTO> getNearbyHotels(double d, double e);



}
