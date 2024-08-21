package com.bangIt.blended.common.bot;

import org.springframework.stereotype.Service;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class KomoranService {

    private final Komoran komoran;

    public Set<String> extractNouns(String text) {
        KomoranResult result = komoran.analyze(text);
        return result.getNouns().stream().collect(Collectors.toSet());
    }
}
