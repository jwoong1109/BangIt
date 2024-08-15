package com.bangIt.blended.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bangIt.blended.domain.entity.ReservationEntity;

public interface ReservationEntityRepository extends JpaRepository<ReservationEntity, Long> {

}
