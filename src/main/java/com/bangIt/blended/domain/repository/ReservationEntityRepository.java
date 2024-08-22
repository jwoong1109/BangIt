package com.bangIt.blended.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bangIt.blended.domain.entity.ReservationEntity;
import com.bangIt.blended.domain.entity.UserEntity;

public interface ReservationEntityRepository extends JpaRepository<ReservationEntity, Long> {

	Optional<ReservationEntity> findById(Long id);

	List<ReservationEntity> findByUser(UserEntity user);

	 //Optional<ReservationEntity> findById(Long id); // 기본 제공 메서드

	 //Optional<ReservationEntity> findByOrderId(String orderId);

	 //ReservationEntity findTopByOrderByIdDesc();
}
