package com.bangIt.blended.service.partner.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bangIt.blended.domain.dto.partner.PartnerSaveDTO;
import com.bangIt.blended.domain.entity.PartnerEntity;
import com.bangIt.blended.domain.entity.Role;
import com.bangIt.blended.domain.entity.UserEntity;
import com.bangIt.blended.domain.repository.PartnerEntityRepository;
import com.bangIt.blended.domain.repository.UserEntityRepository;
import com.bangIt.blended.service.partner.BusinessRegistrationService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BusinessRegistrationServiceProcess implements BusinessRegistrationService {
	
	private final UserEntityRepository userRepository;
	private final PartnerEntityRepository repository;

	/*
	 * @Override public void registerBusinessMember(PartnerSaveDTO dto) {
	 * repository.save(dto.toEntity());
	 * 
	 * }
	 */

	/*
	 * @Override public void registerBusiness(Long userId, String businessNumber) {
	 * Optional<UserEntity> userOptional = userRepository.findById(userId); if
	 * (userOptional.isPresent()) { UserEntity user = userOptional.get();
	 * PartnerEntity partner = PartnerEntity.builder()
	 * .businessNumber(Long.parseLong(businessNumber)) .user(user) .build();
	 * user.addRole(Role.PARTNER); // Partner 역할 부여 user.addPartner(partner); //
	 * User와 Partner 연관 repository.save(partner); userRepository.save(user); }
	 * 
	 * }
	 */

	@Override
	public void registerBusiness(PartnerSaveDTO dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PartnerEntity partnerEntity = PartnerEntity.createPartner(dto.getBusinessNumber(), user);

        repository.save(partnerEntity);
    }
}
