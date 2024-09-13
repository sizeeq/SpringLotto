package com.gmail.sizeeq.lotto.domain.numbergenerator.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class WinningNumbersDto {

    Set<Integer> winningNumbers;
    LocalDateTime date;
}
