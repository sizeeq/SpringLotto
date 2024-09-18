package com.gmail.sizeeq.lotto.domain.resultchecker.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResultDto(
        String hash,
        Set<Integer> numbers,
        Set<Integer> hitNumbers,
        LocalDateTime date,
        boolean isWinner
) {
}
