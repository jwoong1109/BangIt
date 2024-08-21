package com.bangIt.blended.common.bot;

import java.util.List;
import com.bangIt.blended.common.bot.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AnswerEntity에 대한 데이터 액세스를 제공하는 리포지토리 인터페이스
 */
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
	/**
	 * 주어진 intent에 해당하는 응답들을 검색합니다.
	 * 
	 * @param intent 검색할 intent
	 * @return intent에 해당하는 응답 리스트
	 */
	List<AnswerEntity> findByIntent(String intent);
}