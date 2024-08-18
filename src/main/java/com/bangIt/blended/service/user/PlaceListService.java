package com.bangIt.blended.service.user;

import java.util.List;

import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.placesList.SearchPlaceDTO;

public interface PlaceListService {

	void findPlaceProcess(SearchPlaceDTO dTO, Model model);

	void applyFilters(SearchPlaceDTO dto, List<String> accommodationTypes, List<String> themes, Model model);

	void scrapeProcess(String placeName, Model model);

}
