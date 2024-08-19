package com.bangIt.blended.common.bot;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;

/**
 * KomoranService 클래스는 Komoran 형태소 분석기를 사용하여 
 * 입력된 질문을 분석하고 명사를 추출하는 서비스를 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class KomoranService {
    // Komoran 형태소 분석기 객체
    private final Komoran komoran;

    /**
     * 입력된 질문을 분석하고 명사만 추출하여 반환하는 메서드
     * 
     * @param question 분석할 질문 문자열
     * @return 추출된 명사들의 Set
     */
    public Set<String> analyzeTokenAndGetNouns(String question) {
        // Komoran 형태소 분석기를 사용하여 질문 내용을 분석
        KomoranResult analyzeResult = komoran.analyze(question);
        
        // 분석된 결과 중 명사만 추출하여 Set으로 반환
        // Set을 사용하여 중복된 명사를 제거
        Set<String> nouns = analyzeResult.getNouns().stream()
                            .collect(Collectors.toSet());
        
        // 디버깅을 위해 추출된 명사들을 콘솔에 출력
        for (String noun : nouns) {
            System.out.println("추출된 명사: " + noun);
        }
        
        return nouns;
    }
}