package com.bangIt.blended.service.admin;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.revenue.SearchRservationDTO;

public interface AdminRevenueService {

	void getPartnerRevenue(SearchRservationDTO dto, Model model);


	ByteArrayResource exportToExcel(SearchRservationDTO dto);

}
