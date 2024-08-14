package com.bangIt.blended.service.partner.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bangIt.blended.domain.dto.partner.PartnerSaveDTO;
import com.bangIt.blended.domain.entity.PartnerEntity;
import com.bangIt.blended.domain.entity.UserEntity;
import com.bangIt.blended.domain.enums.AuthProvider;
import com.bangIt.blended.domain.enums.Role;
import com.bangIt.blended.domain.repository.PartnerEntityRepository;
import com.bangIt.blended.domain.repository.UserEntityRepository;
import com.bangIt.blended.service.partner.BusinessRegistrationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
public class BusinessRegistrationServiceProcess implements BusinessRegistrationService {

    private final UserEntityRepository userRepository;
    private final PartnerEntityRepository partnerRepository;

    @Override
    @Transactional
    public void registerBusiness(long businessNumber, String socialId, String providerName) {
        System.out.println("Registering business for socialId: " + socialId + " and provider: " + providerName);

        // AuthProvider enum  변환 및 확인
        AuthProvider provider;
        try {
            provider = AuthProvider.valueOf(providerName.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid provider name: " + providerName);
            throw new IllegalArgumentException("Invalid provider name: " + providerName);
        }

        // 사용자 검색 및 예외 처리
        Optional<UserEntity> userOptional = userRepository.findBySocialIdAndProvider(socialId, provider);
        if (userOptional.isEmpty()) {
            System.out.println("User not found with socialId: " + socialId + " and provider: " + providerName);
            throw new IllegalArgumentException("User not found with socialId: " + socialId + " and provider: " + providerName);
        }

        UserEntity user = userOptional.get();

        // 파트너 엔티티 생성 및 저장
        PartnerEntity partner = PartnerEntity.builder()
                .businessNumber(businessNumber)
                .user(user)
                .build();
        partnerRepository.save(partner);
        System.out.println("Partner entity saved for user with socialId: " + socialId);

        // 사용자의 롤 업데이트 (PARTNER 롤 추가)
        user.addRole(Role.PARTNER);
        userRepository.save(user);
        System.out.println("User role updated to PARTNER for socialId: " + socialId);
    }
}
