package com.gmail.sizeeq.lotto.domain.numberreceiver;

import com.gmail.sizeeq.lotto.AdjustableClock;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class NumberReceiverFacadeTest {

    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    Clock clock = Clock.systemUTC();

    @Test
    public void should_return_input_success_response_when_user_gives_six_numbers_in_range() {
        //given
        HashGeneratorTestImpl hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGenerator, clock, ticketRepository);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        TicketDto generatedTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbersFromUser(numbersFromUser)
                .drawDate(nextDrawDate)
                .build();

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        NumberReceiverResponseDto expectedResult = new NumberReceiverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.message);
        assertThat(response).isEqualTo(expectedResult);

    }

    @Test
    public void should_return_not_in_range_response_when_user_gives_six_numbers_but_at_least_one_is_out_of_range() {
        //given
        HashGeneratorTestImpl hashGeneratorTest = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGeneratorTest, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 201, 3, 4, 5, 6);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        NumberReceiverResponseDto expectedResult = new NumberReceiverResponseDto(null, ValidationResult.NUMBERS_NOT_IN_RANGE.message);
        assertThat(response).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_not_in_range_response_when_user_gives_six_numbers_but_at_least_one_is_negative_number() {
        //given
        HashGeneratorTestImpl hashGeneratorTest = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGeneratorTest, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(-1, 2, 3, 4, 5, 6);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        NumberReceiverResponseDto expectedResult = new NumberReceiverResponseDto(null, ValidationResult.NUMBERS_NOT_IN_RANGE.message);
        assertThat(response).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_not_six_numbers_given_response_when_user_gives_less_than_six_numbers() {
        //given
        HashGeneratorTestImpl hashGeneratorTest = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGeneratorTest, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        NumberReceiverResponseDto expectedResult = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.message);
        assertThat(response).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_not_six_numbers_given_response_when_user_gives_more_than_six_numbers() {
        //given
        HashGeneratorTestImpl hashGeneratorTest = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGeneratorTest, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        //then
        NumberReceiverResponseDto expectedResult = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.message);
        assertThat(response).isEqualTo(expectedResult);
    }

    @Test
    public void generator_should_return_correct_hash() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        String generatedHash = response.ticketDto().hash();

        //then
        assertAll(
                () -> assertThat(generatedHash).hasSize(36),
                () -> assertThat(generatedHash).isNotNull()
        );
    }

    @Test
    public void should_return_correct_draw_date() {
        //given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 9, 5, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime generatedDrawDate = response.ticketDto().drawDate();

        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 9, 7, 12, 0, 0);
        assertThat(generatedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_when_date_is_exactly_Saturday_noon() {
        //given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 9, 7, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime generatedDrawDate = response.ticketDto().drawDate();

        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 9, 14, 12, 0, 0);
        assertThat(generatedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_when_date_is_exactly_Saturday_afternoon() {
        //given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 9, 7, 14, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        //when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime generatedDrawDate = response.ticketDto().drawDate();

        //then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 9, 14, 12, 0, 0);
        assertThat(generatedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_empty_list_if_there_are_no_active_tickets() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 9, 7, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGenerator, clock, ticketRepository);
        LocalDateTime drawDate = LocalDateTime.now(clock);

        //when
        List<TicketDto> ticketsByNextDrawDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);

        //then
        assertThat(ticketsByNextDrawDate).isEmpty();
    }

    @Test
    public void should_return_empty_list_if_given_date_is_after_next_draw_date() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 9, 7, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGenerator, clock, ticketRepository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        //when
        List<TicketDto> ticketsByNextDrawDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1));

        //then
        assertThat(ticketsByNextDrawDate).isEmpty();
    }

    @Test
    public void should_return_tickets_with_correct_draw_date() {
        //given
        HashGenerable hashGenerator = new HashGenerator();
        AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2024, 9, 12, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createNumberReceiverFacadeForTest(hashGenerator, clock, ticketRepository);

        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(6, 5, 4, 3, 2, 1));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(Set.of(7, 8, 9, 10, 11, 12));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(Set.of(13, 14, 15, 16, 17, 18));

        TicketDto ticketDto = numberReceiverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();

        LocalDateTime drawDate = ticketDto.drawDate();

        //when
        List<TicketDto> ticketsByNextDrawDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);

        //then
        assertAll(
                () -> assertThat(ticketsByNextDrawDate).containsOnly(ticketDto, ticketDto1),
                () -> assertThat(ticketsByNextDrawDate).hasSize(2)
        );
    }
}