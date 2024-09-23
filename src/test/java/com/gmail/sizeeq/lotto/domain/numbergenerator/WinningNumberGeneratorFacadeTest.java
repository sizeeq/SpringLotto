package com.gmail.sizeeq.lotto.domain.numbergenerator;

import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.gmail.sizeeq.lotto.domain.numbergenerator.exception.WinningNumbersNotFoundException;
import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WinningNumberGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository = new WinningNumberRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    @Test
    public void should_return_set_of_required_size() {
        //given
        RandomNumberGenerable generator = new SecureRandomGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumberGeneratorFacade winningNumberGeneratorFacade = new NumberGeneratorConfiguration().createWinningNumberGeneratorFacadeForTest(generator, winningNumbersRepository, numberReceiverFacade);

        //when
        WinningNumbersDto generatedNumbers = winningNumberGeneratorFacade.generateWinningNumbers();

        //then
        assertThat(generatedNumbers.getWinningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void should_return_set_of_required_size_with_numbers_within_required_range() {
        //given
        RandomNumberGenerable generator = new SecureRandomGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumberGeneratorFacade winningNumberGeneratorFacade = new NumberGeneratorConfiguration().createWinningNumberGeneratorFacadeForTest(generator, winningNumbersRepository, numberReceiverFacade);

        //when
        WinningNumbersDto generatedNumbers = winningNumberGeneratorFacade.generateWinningNumbers();

        //then
        int upperBound = 99;
        int lowerBound = 1;
        Set<Integer> winningNumbers = generatedNumbers.getWinningNumbers();
        boolean numbersInRange = winningNumbers.stream()
                .allMatch(number -> number >= lowerBound && number <= upperBound);
        assertThat(numbersInRange).isTrue();
    }

    @Test
    public void should_throw_an_exception_when_number_not_in_range() {
        //given
        Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 100);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl(numbers);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumberGeneratorFacade winningNumberGeneratorFacade = new NumberGeneratorConfiguration().createWinningNumberGeneratorFacadeForTest(generator, winningNumbersRepository, numberReceiverFacade);

        //when && then
        assertThrows(IllegalStateException.class, winningNumberGeneratorFacade::generateWinningNumbers, "Number is out of range");
    }

    @Test
    public void should_return_set_with_unique_numbers() {
        //given
        RandomNumberGenerable generator = new SecureRandomGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumberGeneratorFacade winningNumberGeneratorFacade = new NumberGeneratorConfiguration().createWinningNumberGeneratorFacadeForTest(generator, winningNumbersRepository, numberReceiverFacade);

        //when
        WinningNumbersDto winningNumbersDto = winningNumberGeneratorFacade.generateWinningNumbers();

        //then
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();
        assertThat(winningNumbers).hasSize(6);
    }

    @Test
    public void should_return_winning_number_by_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 9, 12, 12, 0, 0);
        Set<Integer> numberFromUser = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();

        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .date(drawDate)
                .winningNumbers(numberFromUser)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumberGenerable generator = new SecureRandomGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumberGeneratorFacade winningNumberGeneratorFacade = new NumberGeneratorConfiguration().createWinningNumberGeneratorFacadeForTest(generator, winningNumbersRepository, numberReceiverFacade);

        //when
        WinningNumbersDto winningNumbersDto = winningNumberGeneratorFacade.retrieveWinningNumbersByDate(drawDate);

        //then
        WinningNumbersDto expectedWinningNumbersDto = WinningNumbersDto.builder()
                .date(drawDate)
                .winningNumbers(numberFromUser)
                .build();

        assertThat(winningNumbersDto).isEqualTo(expectedWinningNumbersDto);
    }

    @Test
    public void should_throw_an_exception_when_failed_to_retrieve_numbers_by_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 9, 12, 12, 0, 0);
        RandomNumberGenerable generator = new SecureRandomGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumberGeneratorFacade winningNumberGeneratorFacade = new NumberGeneratorConfiguration().createWinningNumberGeneratorFacadeForTest(generator, winningNumbersRepository, numberReceiverFacade);

        //when & then
        assertThrows(WinningNumbersNotFoundException.class, () -> winningNumberGeneratorFacade.retrieveWinningNumbersByDate(drawDate), "Numbers for given date were not found!");
    }

    @Test
    public void should_return_true_when_numbers_are_generated_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2024, 9, 12, 12, 0, 0);
        Set<Integer> numberFromUser = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .date(drawDate)
                .winningNumbers(numberFromUser)
                .build();
        winningNumbersRepository.save(winningNumbers);

        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumberGeneratorFacade winningNumberGeneratorFacade = new NumberGeneratorConfiguration().createWinningNumberGeneratorFacadeForTest(generator, winningNumbersRepository, numberReceiverFacade);

        //when
        boolean areWinningNumbersGeneratedByDate = winningNumberGeneratorFacade.areWinningNumbersGeneratedByDate();

        //then
        assertThat(areWinningNumbersGeneratedByDate).isTrue();
    }
}