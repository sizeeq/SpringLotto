package com.gmail.sizeeq.lotto.domain.resultannouncer;

import com.gmail.sizeeq.lotto.domain.resultannouncer.dto.ResponseDto;

public class ResultMapper {

    public static ResponseDto mapResultResponseToDto(ResultResponse resultResponse) {
        return ResponseDto.builder()
                .hash(resultResponse.hash())
                .numbers(resultResponse.numbers())
                .hitNumbers(resultResponse.hitNumbers())
                .drawDate(resultResponse.drawDate())
                .isWinner(resultResponse.isWinner())
                .build();
    }
}
