package com.bangIt.blended.domain.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.bangIt.blended.domain.dto.revenue.GetPartnerRevenueDTO;
import com.bangIt.blended.domain.dto.revenue.GetTotalRevenueDTO;
import com.bangIt.blended.domain.dto.revenue.SearchRservationDTO;

@Mapper
public interface RevenueMapper {
	
    @Select("SELECT * FROM partner_revenue_view " +
            "WHERE last_payment_date BETWEEN #{dto.startDate} AND #{dto.endDate} " +
            "AND (#{dto.userEmail} IS NULL OR #{dto.userEmail} = '' OR user_email = #{dto.userEmail})")
    List<GetPartnerRevenueDTO> getPartnerRevenue(@Param("dto") SearchRservationDTO dto);

    @Select("SELECT COALESCE(SUM(total_revenue), 0) as totalRevenue, " +
            "COALESCE(SUM(total_reservations), 0) as totalReservations, " +
            "COALESCE(SUM(commission), 0) as totalCommission " +
            "FROM partner_revenue_view " +
            "WHERE last_payment_date BETWEEN #{dto.startDate} AND #{dto.endDate}")
    GetTotalRevenueDTO getTotalRevenue(@Param("dto") SearchRservationDTO dto);

}
