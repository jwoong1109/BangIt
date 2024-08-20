package com.bangIt.blended.service.admin.impl;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.bangIt.blended.domain.dto.revenue.GetPartnerRevenueDTO;
import com.bangIt.blended.domain.dto.revenue.GetTotalRevenueDTO;
import com.bangIt.blended.domain.dto.revenue.SearchRservationDTO;
import com.bangIt.blended.domain.mapper.RevenueMapper;
import com.bangIt.blended.service.admin.AdminRevenueService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminRevenueServiceProcess implements AdminRevenueService {
	
	 private final RevenueMapper revenueMapper;
	
	//조회하기 
	@Override
	public void getPartnerRevenue(SearchRservationDTO dto, Model model) {
		
		 System.out.println("Search DTO: " + dto);
		
		 List<GetPartnerRevenueDTO> partnerRevenues = revenueMapper.getPartnerRevenue(dto);
		 System.out.println("Partner Revenues: " + partnerRevenues);
		 
		 
		 GetTotalRevenueDTO totalRevenue = revenueMapper.getTotalRevenue(dto);
		 
		 System.out.println("totalRevenue: " + totalRevenue);

	        model.addAttribute("partnerRevenues", partnerRevenues);
	        model.addAttribute("totalRevenue", totalRevenue);
	        model.addAttribute("searchDto", dto);
		
	}

	@Override
	 public ByteArrayResource exportToExcel(SearchRservationDTO dto) {
        List<GetPartnerRevenueDTO> partnerRevenues = revenueMapper.getPartnerRevenue(dto);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Revenue Data");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("판매자 이메일");
            headerRow.createCell(1).setCellValue("총 예약 건수");
            headerRow.createCell(2).setCellValue("총 매출 금액");
            headerRow.createCell(3).setCellValue("수수료");
            headerRow.createCell(4).setCellValue("정산 금액");

            // Populate data rows
            int rowNum = 1;
            for (GetPartnerRevenueDTO revenue : partnerRevenues) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(revenue.getUserEmail());
                row.createCell(1).setCellValue(revenue.getTotalReservations());
                row.createCell(2).setCellValue(revenue.getTotalRevenue().doubleValue());
                row.createCell(3).setCellValue(revenue.getCommission().doubleValue());
                row.createCell(4).setCellValue(revenue.getSettlementAmount().doubleValue());
            }

            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel", e);
        }
    }

}
