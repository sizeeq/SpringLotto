package com.gmail.sizeeq.lotto.domain.numbergenerator;

import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

class SecureRandomGenerator implements RandomNumberGenerable {

    private final int LOWER_BOUND = 1;
    private final int UPPER_BOUND = 99;

    private final int RANDOM_NUMBER_BOUND = (UPPER_BOUND - LOWER_BOUND) + 1;

    @Override
    public SixRandomNumbersDto generateSixNumbers() {
        Set<Integer> winingNumbers = new HashSet<>();
        while (isNumberCountLowerThanSix(winingNumbers)) {
            int randomNumber = generateRandomNumber();
            winingNumbers.add(randomNumber);
        }
        return SixRandomNumbersDto.builder()
                .numbers(winingNumbers)
                .build();
    }

    private boolean isNumberCountLowerThanSix(Set<Integer> numbers) {
        return numbers.size() < 6;
    }

    private int generateRandomNumber() {
        SecureRandom random = new SecureRandom();
        return random.nextInt(RANDOM_NUMBER_BOUND) + 1;
    }
}
