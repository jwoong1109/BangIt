package com.bangIt.blended.common.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * BotService 인터페이스의 구현 클래스입니다. 챗봇의 주요 처리 로직을 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class BotServiceProcess implements BotService {
    private static final Logger logger = LoggerFactory.getLogger(BotServiceProcess.class);

    private final KomoranService komoranService;
    private final AnswerRepository answerRepository;
    private final Random random = new Random();

    @Override
    public String processInput(String input) {
        logger.info("Processing input: {}", input);

        List<String> keywords = extractKeywords(input);
        logger.info("Extracted keywords: {}", keywords);

        if (keywords.isEmpty()) {
            logger.warn("No keywords extracted from input: {}", input);
            return "죄송합니다. 입력을 이해하지 못했습니다. 다른 방식으로 질문해 주시겠어요?";
        }

        List<AnswerEntityDTO> possibleAnswers = new ArrayList<>();
        for (String keyword : keywords) {
            List<AnswerEntityDTO> answers = findAnswersByKeyword(keyword);
            logger.info("Found {} answers for keyword '{}': {}", answers.size(), keyword, answers);
            possibleAnswers.addAll(answers);
        }

        if (possibleAnswers.isEmpty()) {
            logger.warn("No answers found for keywords: {}", keywords);
            return "죄송합니다. 관련된 답변을 찾지 못했습니다. 다른 질문을 해주시겠어요?";
        }

        AnswerEntityDTO selectedAnswer = selectRandomAnswer(possibleAnswers);
        logger.info("Selected answer: {}", selectedAnswer);

        return selectedAnswer.getContent();
    }

    public List<String> extractKeywords(String input) {
        logger.info("Extracting keywords from input: {}", input);
        List<String> keywords = komoranService.extractNouns(input);
        logger.info("Extracted keywords: {}", keywords);
        return keywords;
    }

    private List<AnswerEntityDTO> findAnswersByKeyword(String keyword) {
        logger.info("Finding answers for keyword: {}", keyword);
        List<AnswerEntity> answers = answerRepository.findByIntent(keyword);
        logger.info("Found {} answers in database", answers.size());
        return answers.stream().map(AnswerEntityDTO::fromEntity).collect(Collectors.toList());
    }

    private AnswerEntityDTO selectRandomAnswer(List<AnswerEntityDTO> answers) {
        AnswerEntityDTO selected = answers.get(random.nextInt(answers.size()));
        logger.info("Randomly selected answer: {}", selected);
        return selected;
    }
}
