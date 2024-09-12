package com.gmail.sizeeq.lotto.domain.numberreceiver;

import com.gmail.sizeeq.lotto.AdjustableClock;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.InputNumbersResultDto;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceiverFacadeTest {

    AdjustableClock clock = new AdjustableClock(
            LocalDateTime.of(2023, 2, 15, 12, 0, 0).toInstant(ZoneOffset.UTC),
            ZoneId.of("UTC")
    );

    NumberReceiverFacade facade = new NumberReceiverFacade(
            new NumberValidator(),
            new NumberReceiverRepositoryTestImpl(),
            clock
    );

    @Test
    public void should_return_success_when_user_gives_6_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        InputNumbersResultDto inputNumbersResultDto = facade.inputNumbers(numbersFromUser);

        //then
        assertThat(inputNumbersResultDto.message()).isEqualTo("success");

    }

    @Test
    public void should_return_fail_when_user_gives_number_out_of_range() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 200, 3, 4, 5, 6);

        //when
        InputNumbersResultDto inputNumbersResultDto = facade.inputNumbers(numbersFromUser);

        //then
        assertThat(inputNumbersResultDto.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_fail_when_user_gives_less_than_6_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 200, 3, 4, 5);

        //when
        InputNumbersResultDto inputNumbersResultDto = facade.inputNumbers(numbersFromUser);

        //then
        assertThat(inputNumbersResultDto.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_object_saved_to_database_when_user_gives_6_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumbersResultDto result = facade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = LocalDateTime.of(2023, 2, 15, 12, 0, 0);

        //when
        List<TicketDto> ticketDtos = facade.userNumbers(drawDate);

        //then
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .ticketId(result.ticketId())
                        .drawDate(drawDate)
                        .numbersFromUser(result.numberFromUser())
                        .build()
        );

    }
}