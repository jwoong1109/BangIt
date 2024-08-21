package com.bangIt.blended.domain.entity;
import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicUpdate;
import com.bangIt.blended.domain.enums.PaymentMethod;
import com.bangIt.blended.domain.enums.PaymentStatus;
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

@Entity
@Table(name = "payment")
@DynamicUpdate
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 결제 ID

    @Column(nullable = false, unique = true)
    private String orderId; // 결제 고유 식별자 (UUID 등으로 생성)

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

    private String tossPaymentId; // 토스페이 고유 식별자

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationEntity reservation;

    @OneToOne
    @JoinColumn(name = "sale_id") // SaleEntity와의 연결을 위한 외래키 설정
    private SaleEntity sale;

    
    // 결제 정보 업데이트 메서드
    public void updatePaymentInfo(String tossPaymentId, PaymentStatus paymentStatus, Long amount) {
        this.tossPaymentId = tossPaymentId;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
    }
}
