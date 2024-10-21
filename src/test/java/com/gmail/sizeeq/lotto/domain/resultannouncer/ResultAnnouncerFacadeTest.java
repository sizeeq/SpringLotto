package com.gmail.sizeeq.lotto.domain.resultannouncer;

import com.gmail.sizeeq.lotto.domain.resultannouncer.dto.ResponseDto;
import com.gmail.sizeeq.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import com.gmail.sizeeq.lotto.domain.resultchecker.ResultCheckerFacade;
import com.gmail.sizeeq.lotto.domain.resultchecker.dto.ResultDto;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

import static com.gmail.sizeeq.lotto.domain.resultannouncer.MessageResponse.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultAnnouncerFacadeTest {

    private final ResponseRepository responseRepository = new ResponseRepositoryTestImpl();
    private final ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);

    @Test
    public void should_return_response_with_lose_message_if_ticket_did_not_win() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 9, 14, 12, 0, 0);
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, responseRepository, Clock.systemUTC());
        String hash = "123";
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .date(drawDate)
                .isWinner(false)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);

        //when
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.checkResult(hash);

        //then
        ResponseDto responseDto = ResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.message);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_win_message_if_ticket_won() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 9, 14, 12, 0, 0);
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, responseRepository, Clock.systemUTC());
        String hash = "123";
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .date(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);

        //when
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.checkResult(hash);

        //then
        ResponseDto responseDto = ResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, WIN_MESSAGE.message);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    public void should_return_response_with_wait_message_if_it_is_before_announcement_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 9, 14, 12, 0, 0);
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 9, 13, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, responseRepository, clock);

        String hash = "123";

        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .date(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);

        //when
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.checkResult(hash);

        //then
        ResponseDto responseDto = ResponseDto.builder()
                .hash("123")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.message);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_hash_does_not_exist_message_if_hash_does_not_exist() {
        //given
        String hash = "123";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, responseRepository, Clock.systemUTC());
        when(resultCheckerFacade.findByHash(hash)).thenReturn(null);

        //when
        ResultAnnouncerResponseDto actualResult = resultAnnouncerFacade.checkResult(hash);

        //then
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(null, HASH_DOES_NOT_EXISTS.message);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void should_return_response_with_already_checked_message_if_check_result_was_used_for_hash_for_another_time() {
        //given
        String hash = "123";
        LocalDateTime drawDate = LocalDateTime.of(2024, 9, 14, 12, 0, 0);
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .date(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);

        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().resultAnnouncerFacade(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        String hashTest = resultAnnouncerResponseDto.responseDto().hash();

        //when
        ResultAnnouncerResponseDto actualResponse = resultAnnouncerFacade.checkResult(hashTest);

        //then
        ResultAnnouncerResponseDto expectedResponse = new ResultAnnouncerResponseDto(resultAnnouncerResponseDto.responseDto(), ALREADY_CHECKED.message);
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}