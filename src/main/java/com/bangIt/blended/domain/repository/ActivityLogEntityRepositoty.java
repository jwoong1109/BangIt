package com.bangIt.blended.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bangIt.blended.domain.entity.ActivityLogEntity;


public interface ActivityLogEntityRepositoty extends JpaRepository<ActivityLogEntity, Long> {

}
