package com.gmail.sizeeq.lotto.domain.numbergenerator;

import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.gmail.sizeeq.lotto.domain.numbergenerator.exception.WinningNumbersNotFoundException;
import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class WinningNumberGeneratorFacade {

    private final WinningNumberValidator winningNumberValidator;
    private final RandomNumberGenerable randomNumberGenerator;
    private final WinningNumbersRepository numbersRepository;
    private final NumberReceiverFacade numberReceiver;
    private final WinningFacadeProperties properties;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = numberReceiver.retrieveNextDrawDate();
        SixRandomNumbersDto winningNumbers = randomNumberGenerator.generateSixNumbers(properties.numberCount(), properties.lowestNumber(), properties.highestNumber());
        winningNumberValidator.validateNumbers(winningNumbers.numbers());

        WinningNumbers numbersToSave = WinningNumbers.builder()
                .date(nextDrawDate)
                .winningNumbers(winningNumbers.numbers())
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
