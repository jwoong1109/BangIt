package com.bangIt.blended.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethod {
    CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    EASY_PAY("간편결제"),
    MOBILE_PHONE("휴대폰"),
    BANK_TRANSFER("계좌이체"),
    CULTURE_GIFT_CERTIFICATE("문화상품권"),
    BOOK_CULTURE_CERTIFICATE("도서문화상품권"),
    GAME_CULTURE_CERTIFICATE("게임문화상품권");

    private final String paymentMethodName;

    public String getPaymentMethodName() {
        return paymentMethodName;
    }
}
