package com.bangIt.blended.domain.dto.user.payment;
import com.bangIt.blended.domain.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PaymentRequestDTO {
    private Long reservationId; // 추가된 필드
    private String orderId; // 추가된 필드
    private Long amount;
    private int amountTaxFree;
    private String productDesc;
    private String apiKey;
    private boolean autoExecute;
    private String resultCallback;
    private String retUrl;
    private String retCancelUrl;
    private PaymentMethod paymentMethod; // 기존에 ENUM으로 설정된 결제 방식
    private String paymentKey; // paymentKey 추가
}