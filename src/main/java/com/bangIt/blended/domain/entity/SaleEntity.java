package com.bangIt.blended.domain.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더를 사용하려고 생성자초기화 막아주는것
@NoArgsConstructor
@Entity
@Table(name = "sale")
public class SaleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long amount;

	@Column(nullable = false)
	private LocalDateTime saleDate; // 판매 일자

	@OneToOne
	@JoinColumn(name = "booking_id", nullable = false)
	private ReservationEntity reservation; // 예약 정보와 1대1 관계로 연결
	
	// PaymentEntity와 1대1 관계
    @OneToOne(mappedBy = "sale")
    private PaymentEntity payment;

	@ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller; // 사용자(판매자)와 1대다 관계로 연결

}
