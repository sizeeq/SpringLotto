package com.gmail.sizeeq.lotto.domain.numbergenerator;

import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.gmail.sizeeq.lotto.domain.numbergenerator.exception.WinningNumbersNotFoundException;
import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumberGeneratorFacade {

    private final WinningNumberValidator winningNumberValidator;
    private final RandomNumberGenerable randomNumberGenerator;
    private final WinningNumbersRepository numbersRepository;
    private final NumberReceiverFacade numberReceiver;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = numberReceiver.retrieveNextDrawDate();
        Set<Integer> winningNumbers = randomNumberGenerator.generateSixNumbers();
        winningNumberValidator.validateNumbers(winningNumbers);

        WinningNumbers numbersToSave = WinningNumbers.builder()
                .date(nextDrawDate)
                .winningNumbers(winningNumbers)
                .build();
        numbersRepository.save(numbersToSave);

        return WinningNumbersDto.builder()
                .winningNumbers(numbersToSave.winningNumbers())
                .build();
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime dateTime) {
        WinningNumbers numbersByDate = numbersRepository.findNumbersByDate(dateTime)
                .orElseThrow(() -> new WinningNumbersNotFoundException("Numbers for given date were not found!"));
        return WinningNumbersDto.builder()
                .winningNumbers(numbersByDate.winningNumbers())
                .date(numbersByDate.date())
                .build();
    }

    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = numberReceiver.retrieveNextDrawDate();
        return numbersRepository.existsByDate(nextDrawDate);
    }
}
