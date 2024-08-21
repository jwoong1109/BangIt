package com.bangIt.blended.domain.dto.revenue;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class GetTotalRevenueDTO {
	
    private BigDecimal totalRevenue;
    private Integer totalReservations;
    private BigDecimal totalCommission;

}
