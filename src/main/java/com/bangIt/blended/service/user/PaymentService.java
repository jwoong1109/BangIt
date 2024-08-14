package com.bangIt.blended.service.user;

import com.bangIt.blended.domain.dto.user.payment.PaymentRequestDTO;

public interface PaymentService {


	String createPayment(PaymentRequestDTO requestDTO);

}
