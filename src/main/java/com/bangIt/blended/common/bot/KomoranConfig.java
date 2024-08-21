package com.bangIt.blended.common.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Komoran 형태소 분석기의 설정을 담당하는 클래스입니다.
 * Spring의 @Configuration 애너테이션을 사용하여 컴포넌트 스캔 시
 * 이 클래스를 스프링 빈 설정 클래스에 포함시킵니다.
 */
@Configuration
public class KomoranConfig {
    private static final Logger logger = LoggerFactory.getLogger(KomoranConfig.class);

    /**
     * Komoran 객체를 생성하고 초기화합니다.
     * @return Komoran 형태소 분석기 객체
     */
    @Bean
    public Komoran komoran() {
        // Komoran 객체를 FULL 모델로 초기화합니다.
        // FULL 모델은 Komoran의 가장 포괄적인 모델로 형태소 분석을 위한 전체적인 사전과 규칙을 포함합니다.
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        try {
            // 절대 경로를 사용하여 user.dic 파일을 로드합니다.
            // FileSystemResource를 사용하여 파일 시스템에서 직접 파일을 참조합니다.
            Resource resource = new FileSystemResource("/BangIt/user.dic");
            
            // Resource에서 파일의 절대 경로를 얻습니다.
            String userDicPath = resource.getFile().getAbsolutePath();
            
            // 파일 경로를 로그로 출력하여 디버깅 시 유용하도록 합니다.
            logger.info("Loading user dictionary from: {}", userDicPath);
            
            // Komoran 객체에 사용자 정의 사전을 설정합니다.
            komoran.setUserDic(userDicPath);
            
            // 사용자 정의 사전이 성공적으로 로드되었음을 로그로 기록합니다.
            logger.info("User dictionary loaded successfully.");
        } catch (IOException e) {
            // 파일을 로드하는 도중 IOException이 발생할 경우, 오류를 로그에 기록합니다.
            logger.error("Failed to load user dictionary", e);
        }
        
        // 설정된 Komoran 객체를 반환합니다.
        return komoran;
    }
}
