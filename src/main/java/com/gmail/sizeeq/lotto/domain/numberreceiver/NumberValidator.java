package com.gmail.sizeeq.lotto.domain.numberreceiver;

import java.util.Set;

class NumberValidator {

    private static final int MAX_NUMBERS_FROM_USER = 6;
    private static final int LOWER_BOUND = 1;
    private static final int UPPER_BOUND = 99;

    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .filter(number -> number >= LOWER_BOUND)
                .filter(number -> number <= UPPER_BOUND)
                .count() == MAX_NUMBERS_FROM_USER;
    }
}
