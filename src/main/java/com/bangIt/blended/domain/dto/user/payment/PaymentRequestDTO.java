package com.bangIt.blended.domain.dto.user.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bangIt.blended.domain.entity.PaymentEntity;
import com.bangIt.blended.domain.enums.PaymentMethod;
import com.bangIt.blended.domain.enums.PaymentStatus;

import lombok.Builder;
import lombok.Getter;
import net.minidev.json.JSONObject;

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
}