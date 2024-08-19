package com.bangIt.blended.domain.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bangIt.blended.domain.entity.UserEntity;
import com.bangIt.blended.domain.enums.AuthProvider;


public interface UserEntityRepository extends JpaRepository<UserEntity, Long>{

	Optional <UserEntity> findByEmail(String email);

	Optional <UserEntity> findBySocialIdAndProvider(String socialId, String registrationId);

	Optional<UserEntity> findBySocialIdAndProvider(String socialId, AuthProvider provider);

}
