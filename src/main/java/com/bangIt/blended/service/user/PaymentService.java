package com.bangIt.blended.service.user;

import com.bangIt.blended.domain.dto.room.ReservationDTO;
import com.bangIt.blended.domain.dto.user.payment.PaymentRequestDTO;
import com.bangIt.blended.domain.entity.ReservationEntity;
import com.bangIt.blended.domain.enums.PaymentMethod;

public interface PaymentService {


	String createPayment(PaymentRequestDTO requestDTO);

	boolean validatePayment(String paymentKey, String orderId, Long amount);

	 void savePaymentInfo(String paymentKey, String orderId, Long amount, Long reservationId, PaymentMethod paymentMethod);


	 ReservationDTO getReservationById(Long id);


	Long getAmountByReservationId(Long reservationId);


	
}
