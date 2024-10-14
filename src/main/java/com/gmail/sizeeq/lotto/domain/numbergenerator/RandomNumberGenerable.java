package com.gmail.sizeeq.lotto.domain.numbergenerator;

import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;

public interface RandomNumberGenerable {

    SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand);
}
