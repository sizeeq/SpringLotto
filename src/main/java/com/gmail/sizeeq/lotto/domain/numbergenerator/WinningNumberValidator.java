package com.gmail.sizeeq.lotto.domain.numbergenerator;

import java.util.Set;

class WinningNumberValidator {

    private final int LOWER_BOUND = 1;
    private final int UPPER_BOUND = 99;

    public Set<Integer> validateNumbers(Set<Integer> winningNumbers) {
        if (!areNumbersInRange(winningNumbers)) {
            throw new IllegalStateException("Number is out of range");
        }
        return winningNumbers;
    }

    private boolean areNumbersInRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream()
                .allMatch(n -> n >= LOWER_BOUND && n <= UPPER_BOUND);
    }
}
