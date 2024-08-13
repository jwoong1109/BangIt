package com.bangIt.blended.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bangIt.blended.domain.entity.ImageEntity;

public interface PlaceImageRepository extends JpaRepository<ImageEntity, Long>{

}
