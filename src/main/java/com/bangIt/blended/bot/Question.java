package com.bangIt.blended.bot;

import lombok.Data;

@Data
public class Question {
    private long key;       // 메시지의 고유 식별자
    private String content; // 메시지의 내용
    private String name;    // 발신자 이름 (선택적)
}
