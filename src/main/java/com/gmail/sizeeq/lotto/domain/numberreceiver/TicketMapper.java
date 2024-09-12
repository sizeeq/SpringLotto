package com.gmail.sizeeq.lotto.domain.numberreceiver;

import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.TicketDto;

class TicketMapper {

    public static TicketDto mapFromTicketToTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .drawDate(ticket.drawDate())
                .hash(ticket.hash())
                .numbersFromUser(ticket.numberFromUser())
                .build();
    }


}
