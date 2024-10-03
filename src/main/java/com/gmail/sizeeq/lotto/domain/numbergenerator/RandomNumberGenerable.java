package com.gmail.sizeeq.lotto.domain.numbergenerator;

import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;

public interface RandomNumberGenerable {

    SixRandomNumbersDto generateSixNumbers(int count, int lowerBound, int upperBound);
}
