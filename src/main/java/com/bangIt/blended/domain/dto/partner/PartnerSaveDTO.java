package com.bangIt.blended.domain.dto.partner;




import com.bangIt.blended.domain.entity.PartnerEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Setter
@Getter
public class PartnerSaveDTO {

	private Long userId;
	private Long businessNumber;

	public PartnerEntity toEntity() {
		
		return PartnerEntity.builder()
				.businessNumber(businessNumber)
				.id(userId)
				.build();
	}


}
