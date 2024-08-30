package com.bangIt.blended.common.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bangIt.blended.domain.dto.placesList.WeatherDTO;
import com.bangIt.blended.domain.enums.Region;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class WeatherUtil {
	
	//프로퍼티즈에 설정한 키
	  @Value("${weather.api.key}")
	  private String apiKey;
	  
	  //단기,중기 예보의 요청 엔드 포인트
	  private String shortEndPoint =	"http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0" ;
	  private String midEndPoint =	"http://apis.data.go.kr/1360000/MidFcstInfoService" ;
	  
	  private final RestTemplate restTemplate;
	  private final ObjectMapper objectMapper;
	  
	  
	// 기간에 대한 날씨 정보를 조회하는 메소드
	    public List<WeatherDTO> getWeatherForPeriod(LocalDate checkinDate, LocalDate checkoutDate, Region region) {
	        List<WeatherDTO> weatherList = new ArrayList<>();
	        LocalDate currentDate = LocalDate.now();
	        LocalDate shortTermEndDate = currentDate.plusDays(3);
	        LocalDate midTermEndDate = currentDate.plusDays(10);

	        // 체크아웃 날짜가 10일을 초과하는 경우 null 반환
	        if (checkoutDate.isAfter(midTermEndDate)) {
	            return null;
	        }

	        LocalDate date = checkinDate;
	        while (!date.isAfter(checkoutDate)) {
	            WeatherDTO weatherDTO;
	            if (date.isBefore(shortTermEndDate) || date.isEqual(shortTermEndDate)) {
	                weatherDTO = getShortTermWeather(date, region);
	            } else {
	                weatherDTO = getMidTermWeather(date, region);
	            }
	            weatherList.add(weatherDTO);
	            date = date.plusDays(1);
	        }

	        return weatherList;
	    }

	    // 단기 날씨 정보를 조회하는 메소드
	    private WeatherDTO getShortTermWeather(LocalDate date, Region region) {
	        String url = buildShortTermUrl(date, region);
	        JsonNode response = callApi(url);
	        return parseShortTermResponse(response, date);
	    }

	    // 중기 날씨 정보를 조회하는 메소드
	    private WeatherDTO getMidTermWeather(LocalDate date, Region region) {
	        String url = buildMidTermUrl(date, region);
	        JsonNode response = callApi(url);
	        return parseMidTermResponse(response, date);
	    }

	    // 단기 날씨 API URL 생성 메소드
	    private String buildShortTermUrl(LocalDate date, Region region) {
	        return String.format(shortEndPoint +
	                "?ServiceKey=%s&numOfRows=10&pageNo=1&base_date=%s&base_time=0500&nx=%d&ny=%d",
	                apiKey, date.toString().replace("-", ""), getRegionNx(region), getRegionNy(region));
	    }

	    // 중기 날씨 API URL 생성 메소드
	    private String buildMidTermUrl(LocalDate date, Region region) {
	        return String.format(midEndPoint +
	                "?ServiceKey=%s&numOfRows=10&pageNo=1&regId=%s&tmFc=%s0600",
	                apiKey, getRegionId(region), date.toString().replace("-", ""));
	    }

	    // API 호출 메소드
	    private JsonNode callApi(String url) {
	        try {
	            String response = restTemplate.getForObject(url, String.class);
	            return objectMapper.readTree(response);
	        } catch (Exception e) {
	            throw new RuntimeException("API 호출 중 오류 발생", e);
	        }
	    }

	    // 단기 날씨 응답 파싱 메소드
	    private WeatherDTO parseShortTermResponse(JsonNode response, LocalDate date) {
	        // API 응답 구조에 맞게 파싱 로직 구현
	        // 예시: JsonNode에서 필요한 데이터 추출
	        String weather = response.path("response").path("body").path("items").path("item").get(0).path("fcstValue").asText();
	        double temperature = response.path("response").path("body").path("items").path("item").get(1).path("fcstValue").asDouble();

	        return WeatherDTO.builder()
	                .date(date)
	                .weather(weather)
	                .temperature(temperature)
	                .build();
	    }

	    // 중기 날씨 응답 파싱 메소드
	    private WeatherDTO parseMidTermResponse(JsonNode response, LocalDate date) {
	        // API 응답 구조에 맞게 파싱 로직 구현
	        // 예시: JsonNode에서 필요한 데이터 추출
	        String weather = response.path("response").path("body").path("items").path("item").get(0).path("wf3Am").asText();
	        double maxTemp = response.path("response").path("body").path("items").path("item").get(0).path("taMax3").asDouble();
	        double minTemp = response.path("response").path("body").path("items").path("item").get(0).path("taMin3").asDouble();

	        return WeatherDTO.builder()
	                .date(date)
	                .weather(weather)
	                .maxTemperature(maxTemp)
	                .minTemperature(minTemp)
	                .build();
	    }

	    // Region enum에 따른 nx 값 반환 메소드
	    private int getRegionNx(Region region) {
	        switch (region) {
	            case SEOUL: return 60;
	            // 다른 지역들에 대한 case 추가
	            default: throw new IllegalArgumentException("Invalid region");
	        }
	    }

	    // Region enum에 따른 ny 값 반환 메소드
	    private int getRegionNy(Region region) {
	        switch (region) {
	            case SEOUL: return 127;
	            // 다른 지역들에 대한 case 추가
	            default: throw new IllegalArgumentException("Invalid region");
	        }
	    }

	    // Region enum에 따른 regId 값 반환 메소드
	    private String getRegionId(Region region) {
	        switch (region) {
	            case SEOUL: return "11B10101";
	            case GYEONGGI: return "11B20502";
	            case GANGWON: return "11D20601";
	            case CHUNGCHEONG: return "11C20501";
	            case GYEONGSANG: return "11H20304";
	            case JEOLLA: return "11F20401";
	            case JEJU: return "11G00201";
	            case DOKDO: return "11E00102";
	            
	            // 다른 지역들에 대한 case 추가
	            default: throw new IllegalArgumentException("Invalid region");
	        }
	    }
}
