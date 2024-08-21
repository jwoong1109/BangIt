package com.bangIt.blended.common.bot;

import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KomoranService {
    private static final Logger logger = LoggerFactory.getLogger(KomoranService.class);
    private final Komoran komoran;

    public List<String> extractNouns(String text) {
        logger.info("Extracting nouns from text: {}", text);
        System.out.println("입력 텍스트: " + text); // 콘솔 출력

        KomoranResult analyzeResultList = komoran.analyze(text);
        
        // 형태소 분석 결과 전체를 로깅
        List<Token> tokenList = analyzeResultList.getTokenList();
        logger.info("Full analysis result:");
        for (Token token : tokenList) {
            logger.info("Word: {}, POS: {}", token.getMorph(), token.getPos());
            System.out.println("단어: " + token.getMorph() + ", 품사: " + token.getPos()); // 콘솔 출력
        }

        List<String> nouns = analyzeResultList.getNouns();
        logger.info("Extracted nouns: {}", nouns);
        System.out.println("추출된 명사: " + nouns); // 콘솔 출력

        return nouns;
    }
}