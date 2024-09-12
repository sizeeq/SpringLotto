package com.gmail.sizeeq.lotto.domain.numberreceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class NumberValidator {

    private static final int QUANTITY_NUMBERS_FROM_USER = 6;
    private static final int LOWER_BOUND = 1;
    private static final int UPPER_BOUND = 99;

    List<ValidationResult> errors = new ArrayList<>();

    List<ValidationResult> validate(Set<Integer> numbersFromUser) {
        if (!isNumberSizeEqualSix(numbersFromUser)) {
            errors.add(ValidationResult.NOT_SIX_NUMBERS_GIVEN);
        }
        if (!isNumberInRange(numbersFromUser)) {
            errors.add(ValidationResult.NUMBERS_NOT_IN_RANGE);
        }
        return errors;
    }

    String createResultMessage() {
        return this.errors.stream()
                .map(validationResult -> validationResult.message)
                .collect(Collectors.joining(","));
    }

    private boolean isNumberSizeEqualSix(Set<Integer> numbersFromUser) {
        return numbersFromUser.size() == QUANTITY_NUMBERS_FROM_USER;
    }

    boolean isNumberInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .allMatch(number -> number >= LOWER_BOUND && number <= UPPER_BOUND);
    }
}
