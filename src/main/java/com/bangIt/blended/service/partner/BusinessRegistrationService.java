package com.bangIt.blended.service.partner;

import com.bangIt.blended.domain.dto.partner.PartnerSaveDTO;

public interface BusinessRegistrationService {


	//void registerBusiness(Long userId, String businessNumber);

	//void registerBusiness(PartnerSaveDTO dto);

	void registerBusiness(long businessNumber, String socialId, String providerName);

	

}
