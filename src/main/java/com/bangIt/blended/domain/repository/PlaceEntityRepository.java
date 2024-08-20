package com.bangIt.blended.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bangIt.blended.domain.entity.PlaceEntity;
import com.bangIt.blended.domain.entity.UserEntity;
import com.bangIt.blended.domain.enums.PlaceStatus;

public interface PlaceEntityRepository extends JpaRepository<PlaceEntity, Long>{

	@Query("SELECT p FROM PlaceEntity p JOIN p.rooms r ORDER BY r.roomPrice DESC")
    List<PlaceEntity> findTopByOrderByRoomPriceDesc();

	@Query("SELECT p FROM PlaceEntity p WHERE " +
		       "(6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
		       "cos(radians(p.longitude) - radians(:longitude)) + " +
		       "sin(radians(:latitude)) * sin(radians(p.latitude)))) < :distance " +
		       "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
		       "cos(radians(p.longitude) - radians(:longitude)) + " +
		       "sin(radians(:latitude)) * sin(radians(p.latitude)))) ASC")
	    List<PlaceEntity> findPlacesNearby(@Param("latitude") double latitude,
	                                       @Param("longitude") double longitude,
	                                       @Param("distance") double distance);

	// PENDING_APPROVAL 상태의 Place를 조회하는 메서드 추가
    List<PlaceEntity> findByStatus(PlaceStatus status);
    List<PlaceEntity> findBySeller(UserEntity seller);
    Optional<PlaceEntity> findByIdAndSeller(Long id, UserEntity seller);



	
}
