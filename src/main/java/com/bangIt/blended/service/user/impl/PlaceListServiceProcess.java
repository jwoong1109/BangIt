package com.bangIt.blended.service.user.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.place.PlaceDTO;
import com.bangIt.blended.domain.dto.placesList.SearchPlaceDTO;
import com.bangIt.blended.domain.mapper.PlaceMapper;
import com.bangIt.blended.service.user.PlaceListService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Service
public class PlaceListServiceProcess implements PlaceListService {
	
	private final PlaceMapper placeMapper;

    @Override
    public void findPlaceProcess(SearchPlaceDTO dto, Model model) {
    	    
    	   System.out.println("serchdto: " + dto);
    	    
    	    List<PlaceDTO> places = placeMapper.findprocess(dto);
    	    
    	    
    	    model.addAttribute("placeList", places);
    	    model.addAttribute("searchDto", dto);
    	    System.out.println("places: " + places);
    }

    @Override
    public void applyFilters(SearchPlaceDTO dto, List<String> accommodationTypes, List<String> themes, Model model) {
        dto.setAccommodationTypes(accommodationTypes);
        dto.setThemes(themes);
        List<PlaceDTO> places = placeMapper.findprocess(dto);
        model.addAttribute("placeList", places);
        model.addAttribute("searchDto", dto);
    }

	@Override
	public void scrapeProcess(String placeName, Model model) {
		
		/*https://velog.io/@minjiki2/%ED%81%AC%EB%A1%A4%EB%A7%81-Java%EB%A1%9C-%EC%9B%B9-%ED%81%AC%EB%A1%A4%EB%A7%81%ED%95%98%EA%B8%B0-Jsoup*/		
	  
        try {  
            Document yeogi = Jsoup.connect("https://www.yeogi.com/domestic-accommodations?searchType=KEYWORD&keyword="+placeName)
                                  .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                                  .get();

            // JSON 데이터 추출
            Element scriptElement = yeogi.select("script#__NEXT_DATA__").first();
            if (scriptElement != null) {
                String jsonData = scriptElement.html();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray accommodations = jsonObject.getJSONObject("props")
                                                     .getJSONObject("pageProps")
                                                     .getJSONArray("accommodationsData");

                JSONArray hotels = new JSONArray();
                for (int i = 0; i < accommodations.length(); i++) {
                    JSONObject accommodation = accommodations.getJSONObject(i);
                    JSONObject hotel = new JSONObject();
                    
                    JSONObject meta = accommodation.getJSONObject("meta");
                    hotel.put("name", meta.getString("name"));
                    hotel.put("grade", meta.getString("grade"));
                    hotel.put("imageUrl", meta.getJSONArray("images").getString(0));
                    
                    JSONObject address = meta.getJSONObject("address");
                    hotel.put("location", address.getString("traffic"));
                    
                    JSONObject review = meta.getJSONObject("review");
                    hotel.put("rating", review.getDouble("rate"));
                    hotel.put("reviewCount", review.getInt("count"));
                    
                    JSONObject room = accommodation.getJSONObject("room");
                    if (room.has("stay")) {
                        JSONObject stay = room.getJSONObject("stay");
                        if (stay.has("price")) {
                            JSONObject price = stay.getJSONObject("price");
                            hotel.put("price", price.getInt("discountPrice"));
                        }
                    }
                    
                    hotels.put(hotel);
                }
                
                System.out.println("Scraped hotels: " + hotels.toString());
                model.addAttribute("scrapedHotels", hotels.toString());
            } else {
                System.out.println("JSON data not found");
                model.addAttribute("error", "JSON data not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error message: " + e.getMessage());
            model.addAttribute("error", "Scraping failed: " + e.getMessage());
        }
    
		
	}

}
