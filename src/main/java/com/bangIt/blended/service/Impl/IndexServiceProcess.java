package com.bangIt.blended.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.common.util.ip.GeoLocationService;
import com.bangIt.blended.domain.dto.place.HotelListDTO;
import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.repository.PlaceEntityRepository;
import com.bangIt.blended.service.IndexService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexServiceProcess implements IndexService {

    private final PlaceEntityRepository placeRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public List<HotelListDTO> getNearbyHotels(String ipAddress) {
        double[] userCoordinates = geoLocationService.getCoordinates(); // IP로부터 좌표 가져오기
        return getNearbyHotels(userCoordinates[0], userCoordinates[1]);
    }

    @Override
    public List<HotelListDTO> getNearbyHotels(double userLat, double userLon) {
        List<PlaceEntity> places = placeRepository.findAll(); // 모든 장소를 가져옴
        
        return places.stream()
                     .map(place -> {
                         double placeLat = place.getLatitude();
                         double placeLon = place.getLongitude();
                         double distance = calculateDistance(userLat, userLon, placeLat, placeLon); //거리계산
                         System.out.println("Place: " + place.getName() + ", Distance: " + distance);

                         return place.toHotelListDTOWithDistance(distance); // 거리 정보 포함하여 DTO 변환
                     })
                     .sorted((hotel1, hotel2) -> Double.compare(hotel1.getDistance(), hotel2.getDistance())) // 거리순으로 정렬
                     //.limit(4)
                     .collect(Collectors.toList());
    }

    @Override
    public List<HotelListDTO> TopPriceHotelList(Model model) {
        List<PlaceEntity> topPriceHotels = placeRepository.findTopByOrderByRoomPriceDesc();

        return topPriceHotels.stream()
                             .map(PlaceEntity::toHotelListDTOWithoutDistance) // 거리 정보 포함하지 않은 DTO 변환
                             .collect(Collectors.toList());
    }

    // 거리 계산 메서드
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (킬로미터)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 (킬로미터)
    }
}
