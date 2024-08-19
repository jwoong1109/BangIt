package com.bangIt.blended.controller.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bangIt.blended.domain.dto.placesList.ScrapePlaceDTO;
import com.bangIt.blended.domain.dto.placesList.SearchPlaceDTO;
import com.bangIt.blended.service.user.PlaceListService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Controller
public class PlaceListController {
	
	private final PlaceListService placeListService;
	
    @PostMapping("/search/places")
    public String searchPlaces(SearchPlaceDTO dto, Model model) {
        placeListService.findPlaceProcess(dto, model);
        return "/views/user/placeList/placelist";
    }
    
    
    @ResponseBody
    @GetMapping("/search/places/scrape/{placeName}")
    public ScrapePlaceDTO scrapePlace(@PathVariable(name = "placeName") String placeName) {
    	
    	return placeListService.scrapeProcess(placeName);
        
    }
    
    
 
    

	

}
