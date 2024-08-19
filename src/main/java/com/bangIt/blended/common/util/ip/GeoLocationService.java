package com.bangIt.blended.common.util.ip;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class GeoLocationService {

    private static final String API_URL = "https://ipinfo.io/json";

    @Value("${ipinfo.api.token}")
    private String apiToken;

    public double[] getCoordinates() {
    	
        String url = API_URL + "?token=" + apiToken;
        RestTemplate restTemplate = new RestTemplate();
        String locationJson = restTemplate.getForObject(url, String.class);
        
        // JSON 파싱
        JSONObject jsonObject = new JSONObject(locationJson);
        if (!jsonObject.has("loc")) {
            throw new RuntimeException("Location data not found");
        }

        String loc = jsonObject.getString("loc");
        String[] coordinates = loc.split(",");
        
        if (coordinates.length < 2) {
            throw new RuntimeException("Invalid location data: " + loc);
        }

        return new double[]{Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1])};
    }

    // 필요한 경우 IP 주소 정보 자체를 가져오는 메서드
    public String getIpAddress() {
        String url = API_URL + "?token=" + apiToken;
        RestTemplate restTemplate = new RestTemplate();
        String locationJson = restTemplate.getForObject(url, String.class);
        
        JSONObject jsonObject = new JSONObject(locationJson);
        return jsonObject.getString("ip");
    }
}
