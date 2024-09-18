package com.gmail.sizeeq.lotto.domain.resultchecker;

import com.gmail.sizeeq.lotto.domain.numberreceiver.Ticket;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class WinnersRetriever {

    private static final int NUMBERS_TO_WIN = 3;

    List<Player> retrieveWinners(List<Ticket> allTicketsByDate, Set<Integer> winningNumbers) {
        return allTicketsByDate.stream()
                .map(ticket -> {
                    Set<Integer> hitNumbers = calculateHits(winningNumbers, ticket);
                    return buildNewPlayer(ticket, hitNumbers);
                })
                .toList();
    }

    private Player buildNewPlayer(Ticket ticket, Set<Integer> hitNumbers) {
        boolean isPlayerAWinner = isWinner(hitNumbers);
        return Player.builder()
                .hash(ticket.hash())
                .numbers(ticket.numberFromUser())
                .hitNumbers(hitNumbers)
                .date(ticket.drawDate())
                .isWinner(isPlayerAWinner)
                .build();
    }

    private Set<Integer> calculateHits(Set<Integer> winningNumbers, Ticket ticket) {
        return ticket.numberFromUser().stream()
                .filter(winningNumbers::contains)
                .collect(Collectors.toSet());
    }

    private boolean isWinner(Set<Integer> hitNumbers) {
        return hitNumbers.size() >= NUMBERS_TO_WIN;
    }
}
