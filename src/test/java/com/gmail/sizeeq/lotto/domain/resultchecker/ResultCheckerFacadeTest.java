package com.gmail.sizeeq.lotto.domain.resultchecker;

import com.gmail.sizeeq.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.TicketDto;
import com.gmail.sizeeq.lotto.domain.resultchecker.dto.PlayerDto;
import com.gmail.sizeeq.lotto.domain.resultchecker.dto.ResultDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultCheckerFacadeTest {

    private final PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
    private final WinningNumberGeneratorFacade winningNumberGeneratorFacade = mock(WinningNumberGeneratorFacade.class);
    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    @Test
    public void should_generate_all_players_with_correct_messages() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 9, 21, 12, 0, 0);
        when(winningNumberGeneratorFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                        .build()
        );
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                List.of(
                        TicketDto.builder()
                                .hash("001")
                                .numbersFromUser(new LinkedHashSet<>(Set.of(1, 2, 3, 4, 5, 6)))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("002")
                                .numbersFromUser(new LinkedHashSet<>(Set.of(1, 2, 10, 12, 13, 15)))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("003")
                                .numbersFromUser(new LinkedHashSet<>(Set.of(7, 8, 9, 10, 11, 12)))
                                .drawDate(drawDate)
                                .build()
                )
        );
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createResultCheckerFacadeForTests(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository);

        //when
        PlayerDto playerDto = resultCheckerFacade.generateWinners();

        //then
        List<ResultDto> results = playerDto.results();
        ResultDto resultExpected1 = ResultDto.builder()
                .hash("001")
                .numbers(new LinkedHashSet<>(Set.of(1, 2, 3, 4, 5, 6)))
                .hitNumbers(new LinkedHashSet<>(Set.of(1, 2, 3, 4, 5, 6)))
                .date(drawDate)
                .isWinner(true)
                .build();
        ResultDto resultExpected2 = ResultDto.builder()
                .hash("002")
                .numbers(new LinkedHashSet<>(Set.of(1, 2, 10, 12, 13, 15)))
                .hitNumbers(new LinkedHashSet<>(Set.of(1, 2)))
                .date(drawDate)
                .isWinner(false)
                .build();
        ResultDto resultExpected3 = ResultDto.builder()
                .hash("003")
                .numbers(new LinkedHashSet<>(Set.of(7, 8, 9, 10, 11, 12)))
                .hitNumbers(Set.of())
                .date(drawDate)
                .isWinner(false)
                .build();
        String actualMessage = playerDto.message();

        assertAll(
                () -> assertThat(actualMessage).isEqualTo("Winners retrieved!"),
                () -> assertThat(results).contains(resultExpected1, resultExpected2, resultExpected3)
        );
    }

    @Test
    public void should_return_fail_message_when_winningNumbers_equal_null() {
        //given
        when(winningNumberGeneratorFacade.generateWinningNumbers()).thenReturn(
                WinningNumbersDto.builder()
                        .winningNumbers(null)
                        .build()
        );
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createResultCheckerFacadeForTests(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository);

        //when
        PlayerDto playerDto = resultCheckerFacade.generateWinners();

        //then
        String message = playerDto.message();
        assertThat(message).isEqualTo("Failed to retrieve winners!");
    }

    @Test
    public void should_return_fail_message_when_winningNumbers_is_empty() {
        //given
        when(winningNumberGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of())
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createResultCheckerFacadeForTests(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository);

        //when
        PlayerDto playerDto = resultCheckerFacade.generateWinners();

        //then
        String message = playerDto.message();
        assertThat(message).isEqualTo("Failed to retrieve winners!");
    }

    @Test
    public void should_generate_result_with_correct_credentials() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 9, 21, 12, 0, 0);
        when(winningNumberGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        String hash = "001";
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                List.of(
                        TicketDto.builder()
                                .hash(hash)
                                .numbersFromUser(new LinkedHashSet<>(Set.of(7, 8, 9, 10, 11, 12)))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("002")
                                .numbersFromUser(new LinkedHashSet<>(Set.of(13, 14, 15, 16, 17, 18)))
                                .drawDate(drawDate)
                                .build(),
                        TicketDto.builder()
                                .hash("003")
                                .numbersFromUser(new LinkedHashSet<>(Set.of(19, 20, 21, 22, 23, 24)))
                                .drawDate(drawDate)
                                .build()
                )
        );
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createResultCheckerFacadeForTests(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository);
        resultCheckerFacade.generateWinners();

        //when
        ResultDto resultDto = resultCheckerFacade.findByHash(hash);

        //then
        ResultDto excpectedResultDto = ResultDto.builder()
                .hash(hash)
                .numbers(new LinkedHashSet<>(Set.of(7, 8, 9, 10, 11, 12)))
                .hitNumbers(Set.of())
                .date(drawDate)
                .isWinner(false)
                .build();
        assertThat(resultDto).isEqualTo(excpectedResultDto);
    }
}