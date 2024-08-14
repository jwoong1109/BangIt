package com.bangIt.blended.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bangIt.blended.domain.entity.PaymentEntity;

public interface PaymentEntityRepository extends JpaRepository<PaymentEntity, Long> {

}
