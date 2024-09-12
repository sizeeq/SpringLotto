package com.gmail.sizeeq.lotto.domain.numberreceiver;

import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.TicketDto;

class TicketMapper {

    public static TicketDto mapFromTicketToTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .drawDate(ticket.drawDate())
                .ticketId(ticket.ticketId())
                .numbersFromUser(ticket.numberFromUser())
                .build();
    }
}
