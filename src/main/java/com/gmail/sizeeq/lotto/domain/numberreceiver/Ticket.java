package com.gmail.sizeeq.lotto.domain.numberreceiver;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
public record Ticket(
        @Id String hash,
        Set<Integer> numberFromUser,
        LocalDateTime drawDate
) {
}
