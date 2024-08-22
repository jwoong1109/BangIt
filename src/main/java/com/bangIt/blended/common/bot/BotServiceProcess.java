package com.bangIt.blended.common.bot;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotServiceProcess implements BotService {

    private final KomoranService komoranService;
    private final AnswerRepository answerRepository;
    private final Random random = new Random();

    @Override
    public String processInput(String input) {
        // 사용자 입력에서 키워드를 추출합니다.
        Set<String> keywords = extractKeywords(input);
        System.out.println("Extracted keywords: " + keywords); // 디버깅: 추출된 키워드 출력

        // 키워드가 비어있는 경우 처리
        if (keywords.isEmpty()) {
            return "죄송합니다. 입력을 이해하지 못했습니다. 다른 방식으로 질문해 주시겠어요?";
        }

        // 키워드에 맞는 답변을 찾습니다.
        List<AnswerEntityDTO> possibleAnswers = new ArrayList<>();
        for (String keyword : keywords) {
            List<AnswerEntityDTO> answers = findAnswersByKeyword(keyword);
            System.out.println("Found answers for keyword '" + keyword + "': " + answers); // 디버깅: 찾은 답변 출력
            possibleAnswers.addAll(answers);
        }

        // 답변을 찾지 못한 경우 처리
        if (possibleAnswers.isEmpty()) {
            return "죄송합니다. 관련된 답변을 찾지 못했습니다. 다른 질문을 해주시겠어요?";
        }

        // 답변을 랜덤으로 선택합니다.
        AnswerEntityDTO selectedAnswer = selectRandomAnswer(possibleAnswers);
        return selectedAnswer.getContent();
    }

    @Override
    public Set<String> extractKeywords(String input) {
        return komoranService.extractNouns(input);
    }

    private List<AnswerEntityDTO> findAnswersByKeyword(String keyword) {
        List<AnswerEntity> answers = answerRepository.findByIntent(keyword);
        return answers.stream().map(AnswerEntityDTO::fromEntity).collect(Collectors.toList());
    }

    private AnswerEntityDTO selectRandomAnswer(List<AnswerEntityDTO> answers) {
        return answers.get(random.nextInt(answers.size()));
    }

	@Override
	public void handleUserQuery(Question dto) {
		
	}
}
