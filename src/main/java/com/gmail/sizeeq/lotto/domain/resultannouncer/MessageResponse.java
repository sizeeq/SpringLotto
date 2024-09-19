package com.gmail.sizeeq.lotto.domain.resultannouncer;

public enum MessageResponse {

    HASH_DOES_NOT_EXISTS("Ticket was not found."),
    WIN_MESSAGE("Congratulation, you won!"),
    LOSE_MESSAGE("Not today, try again!"),
    ALREADY_CHECKED("You already checked the ticket!"),
    WAIT_MESSAGE("Result are being calculated, please wait.");

    final String message;

    MessageResponse(String message) {
        this.message = message;
    }
}
