package com.bangIt.blended.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bangIt.blended.domain.entity.PaymentEntity;
import com.bangIt.blended.domain.entity.ReservationEntity;

public interface PaymentEntityRepository extends JpaRepository<PaymentEntity, Long> {

	 Optional<PaymentEntity> findByOrderId(String orderId);

}
