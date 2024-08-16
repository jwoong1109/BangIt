package com.bangIt.blended.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;

import com.bangIt.blended.domain.enums.AuthProvider;
import com.bangIt.blended.domain.enums.PaymentMethod;
import com.bangIt.blended.domain.enums.PaymentStatus;
import com.bangIt.blended.domain.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "payment")
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 결제 ID

	@Column(nullable = false)
	private long amount; // 결제 금액

	@Column(nullable = false)
	private LocalDateTime paymentDate; // 결제 일자

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentMethod paymentMethod; // 결제 방법

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus paymentStatus; // 결제 상태
	
	private String cancelReason; // 결제 취소 사유

	private LocalDateTime cancelDate; // 결제 취소 일자

	private String tossPaymentId; // 토스페이 고유 식별자

	private String responseCode; // API 응답 코드

	private String responseMessage; // API 응답 메시지
	
	
	 // ReservationEntity와 1대1 관계, 외래 키 추가
    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false) // 외래 키 컬럼 추가
    private ReservationEntity reservation;
    
    // SaleEntity와 1대1 관계
    @OneToOne
    @JoinColumn(name = "sale_id")
    private SaleEntity sale;


}


