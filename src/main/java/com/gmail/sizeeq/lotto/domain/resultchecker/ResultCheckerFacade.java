package com.gmail.sizeeq.lotto.domain.resultchecker;

import com.gmail.sizeeq.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.gmail.sizeeq.lotto.domain.numberreceiver.Ticket;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.TicketDto;
import com.gmail.sizeeq.lotto.domain.resultchecker.dto.PlayerDto;
import com.gmail.sizeeq.lotto.domain.resultchecker.dto.ResultDto;
import com.gmail.sizeeq.lotto.domain.resultchecker.exception.PlayerNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

import static com.gmail.sizeeq.lotto.domain.resultchecker.ResultCheckerMapper.mapPlayersToResultDto;

@AllArgsConstructor
public class ResultCheckerFacade {

    private final WinningNumberGeneratorFacade winningNumberGeneratorFacade;
    private final NumberReceiverFacade numberReceiverFacade;
    private final PlayerRepository playerRepository;
    private final WinnersRetriever winnersRetriever;

    public PlayerDto generateWinners() {
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
        List<Ticket> tickets = ResultCheckerMapper.mapFromTicketDtoToTicket(allTicketsByDate);
        WinningNumbersDto winningNumbersDto = winningNumberGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();

        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return PlayerDto.builder()
                    .message("Failed to retrieve winners!")
                    .build();
        }

        List<Player> players = winnersRetriever.retrieveWinners(tickets, winningNumbers);
        playerRepository.saveAll(players);
        return PlayerDto.builder()
                .results(mapPlayersToResultDto(players))
                .message("Winners retrieved!")
                .build();
    }

    public ResultDto findByHash(String hash) {
        Player player = playerRepository.findById(hash)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
        return ResultDto.builder()
                .hash(hash)
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .date(player.date())
                .isWinner(player.isWinner())
                .build();
    }
}
