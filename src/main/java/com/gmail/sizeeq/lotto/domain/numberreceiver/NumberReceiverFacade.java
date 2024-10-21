package com.gmail.sizeeq.lotto.domain.numberreceiver;

import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator numberValidator;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerator;
    private final TicketRepository ticketRepository;

    public NumberReceiverResponseDto inputNumbers(Set<Integer> numbersFromUser) {
        List<ValidationResult> validationResults = numberValidator.validate(numbersFromUser);
        if (!validationResults.isEmpty()) {
            String resultMessage = numberValidator.createResultMessage();
            return new NumberReceiverResponseDto(null, resultMessage);
        }
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        String hash = hashGenerator.getHash();

        TicketDto generatedTicket = TicketDto.builder()
                .hash(hash)
                .drawDate(nextDrawDate)
                .numbersFromUser(numbersFromUser)
                .build();

        Ticket savedTicket = Ticket.builder()
                .hash(generatedTicket.hash())
                .drawDate(generatedTicket.drawDate())
                .numberFromUser(generatedTicket.numbersFromUser())
                .build();
        ticketRepository.save(savedTicket);

        return new NumberReceiverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.message);
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate() {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        return retrieveAllTicketsByNextDrawDate(nextDrawDate);
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllTicketsByDrawDate(date).stream()
                .filter(ticket -> ticket.drawDate().isEqual(date))
                .map(TicketMapper::mapFromTicketToTicketDto)
                .toList();
    }

    public LocalDateTime retrieveNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }

    public TicketDto findByHash(String hash) {
        Ticket ticket = ticketRepository.findByHash(hash);
        return TicketDto.builder()
                .hash(ticket.hash())
                .drawDate(ticket.drawDate())
                .numbersFromUser(ticket.numberFromUser())
                .build();
    }
}
