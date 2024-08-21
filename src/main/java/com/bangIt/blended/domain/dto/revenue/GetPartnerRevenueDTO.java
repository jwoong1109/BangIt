package com.bangIt.blended.domain.dto.revenue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
public class GetPartnerRevenueDTO {

	
    private Long userId;
    private String userEmail;
    private Integer totalReservations;
    private BigDecimal totalRevenue;
    private BigDecimal commission;
    private BigDecimal settlementAmount;
    private LocalDateTime lastPaymentDate;
}
