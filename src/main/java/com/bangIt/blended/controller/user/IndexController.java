package com.bangIt.blended.controller.user;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bangIt.blended.common.util.ip.GeoLocationService;
import com.bangIt.blended.domain.dto.place.HotelListDTO;
import com.bangIt.blended.domain.entity.RoomEntity;
import com.bangIt.blended.service.IndexService;

import jakarta.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Controller
public class IndexController {

	private final IndexService indexService;
	private final GeoLocationService geoLocationService;
	
	
	 @GetMapping("/")
	    public String showHomePage(Model model) {
	        double[] userCoordinates = geoLocationService.getCoordinates(); // 위도와 경도 가져오기
	     // 위도와 경도를 콘솔에 출력
	        System.out.println("User Latitude: " + userCoordinates[0]);
	        System.out.println("User Longitude: " + userCoordinates[1]);
	        
	        List<HotelListDTO> nearbyHotels = indexService.getNearbyHotels(userCoordinates[0], userCoordinates[1]);

	        model.addAttribute("nearbyHotels", nearbyHotels); // 모델에 추가
		
		//가격별 숙소 리스트 가져오기
		List<HotelListDTO> hotelList = indexService.TopPriceHotelList(model);
		model.addAttribute("HotelList", hotelList);
		
		// 이번 달 인기 숙소 리스트 가져오기
        //List<HotelListDTO> topReservedHotels = indexService.getTopReservedHotelsForCurrentMonth(model);
        //model.addAttribute("TopReservedHotels", topReservedHotels);
		return "/index";
	}
	

}
