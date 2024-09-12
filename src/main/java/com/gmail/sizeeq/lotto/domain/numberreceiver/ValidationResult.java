package com.gmail.sizeeq.lotto.domain.numberreceiver;

enum ValidationResult {

    INPUT_SUCCESS("Success!"),
    NUMBERS_NOT_IN_RANGE("Given numbers should be in 1 to 99 range!"),
    NOT_SIX_NUMBERS_GIVEN("You should input 6 numbers!");

    final String message;

    ValidationResult(String message) {
        this.message = message;
    }
}
