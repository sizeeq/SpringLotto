package com.gmail.sizeeq.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;

// encja do bazy
public record Ticket(String ticketId, LocalDateTime drawDate, Set<Integer> numberFromUser) {
}
