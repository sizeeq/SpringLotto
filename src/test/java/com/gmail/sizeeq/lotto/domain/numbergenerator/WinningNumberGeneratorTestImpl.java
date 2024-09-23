package com.gmail.sizeeq.lotto.domain.numbergenerator;

import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;

import java.util.Set;

public class WinningNumberGeneratorTestImpl implements RandomNumberGenerable {

    private final Set<Integer> generatedNumbers;

    WinningNumberGeneratorTestImpl() {
        generatedNumbers = Set.of(1, 2, 3, 4, 5, 6);
    }

    WinningNumberGeneratorTestImpl(Set<Integer> generatedNumbers) {
        this.generatedNumbers = generatedNumbers;
    }

    @Override
    public SixRandomNumbersDto generateSixNumbers() {
        return SixRandomNumbersDto.builder()
                .numbers(generatedNumbers)
                .build();
    }
}
