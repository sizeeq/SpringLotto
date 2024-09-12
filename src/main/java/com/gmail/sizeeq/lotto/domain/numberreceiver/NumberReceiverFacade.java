package com.gmail.sizeeq.lotto.domain.numberreceiver;

import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.InputNumbersResultDto;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final NumberReceiverRepository repository;
    private Clock clock;

    public InputNumbersResultDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean areAllNumbersInRange = numberValidator.areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            String ticketId = UUID.randomUUID().toString();
            LocalDateTime drawDate = LocalDateTime.now(clock);
            Ticket save = repository.save(new Ticket(ticketId, drawDate, numbersFromUser));
            return InputNumbersResultDto.builder()
                    .ticketId(save.ticketId())
                    .drawDate(save.drawDate())
                    .numberFromUser(numbersFromUser)
                    .message("success")
                    .build();
        }
        return InputNumbersResultDto.builder()
                .message("failed")
                .build();
    }

    public List<TicketDto> userNumbers(LocalDateTime date) {
        List<Ticket> allTicketsByDrawDate = repository.findAllTicketsByDrawDate(date);
        return allTicketsByDrawDate.stream()
                .map(TicketMapper::mapFromTicketToTicketDto)
                .toList();
    }
}
