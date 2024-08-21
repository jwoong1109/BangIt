package com.bangIt.blended.common.bot;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * AnswerEntity의 데이터를 전송하기 위한 DTO 클래스
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerEntityDTO {
    private long no;
    private String intent;
    private String content;

    // AnswerEntity를 DTO로 변환하는 정적 메서드
    public static AnswerEntityDTO fromEntity(AnswerEntity entity) {
        return AnswerEntityDTO.builder()
                .no(entity.getNo())
                .intent(entity.getIntent())
                .content(entity.getContent())
                .build();
    }
}
