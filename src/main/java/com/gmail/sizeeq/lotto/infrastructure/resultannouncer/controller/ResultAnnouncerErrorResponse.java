package com.gmail.sizeeq.lotto.infrastructure.resultannouncer.controller;

import org.springframework.http.HttpStatus;

public record ResultAnnouncerErrorResponse(
        String message,
        HttpStatus status
) {
}
