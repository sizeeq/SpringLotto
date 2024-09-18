package com.gmail.sizeeq.lotto.domain.resultchecker;

import com.gmail.sizeeq.lotto.domain.numberreceiver.Ticket;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.TicketDto;
import com.gmail.sizeeq.lotto.domain.resultchecker.dto.ResultDto;

import java.util.List;

class ResultCheckerMapper {

    static List<Ticket> mapFromTicketDtoToTicket(List<TicketDto> allTicketsByDate) {
        return allTicketsByDate.stream()
                .map(ticketDto -> Ticket.builder()
                        .hash(ticketDto.hash())
                        .numberFromUser(ticketDto.numbersFromUser())
                        .drawDate(ticketDto.drawDate())
                        .build())
                .toList();
    }

    static List<ResultDto> mapPlayersToResultDto(List<Player> players) {
        return players.stream()
                .map(player -> ResultDto.builder()
                        .hash(player.hash())
                        .numbers(player.numbers())
                        .hitNumbers(player.hitNumbers())
                        .date(player.date())
                        .isWinner(player.isWinner())
                        .build()
                )
                .toList();
    }
}
