package com.bangIt.blended.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bangIt.blended.domain.entity.UserEntity;


public interface UserEntityRepository extends JpaRepository<UserEntity, Long>{

	Optional<UserEntity> findByEmail(String email);

}
