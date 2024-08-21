package com.bangIt.blended.common.bot;

import java.util.Set;

/**
 * 챗봇 서비스의 인터페이스를 정의합니다.
 */
public interface BotService {
    /**
     * 사용자 입력을 처리하고 적절한 응답을 생성합니다.
     * @param input 사용자 입력 텍스트
     * @return 생성된 챗봇 응답
     */
    String processInput(String input);
    
    
    /**
     * 사용자 입력에서 키워드를 추출합니다.
     * 
     * @param input 사용자 입력 텍스트
     * @return 추출된 키워드 리스트
     */
    Set<String> extractKeywords(String input);
    
}