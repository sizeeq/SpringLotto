package com.gmail.sizeeq.lotto.domain.numbergenerator;

import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NumberGeneratorConfiguration {

    @Bean
    WinningNumberGeneratorFacade winningNumberGeneratorFacade(WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade, RandomNumberGenerable randomNumberGenerator, WinningFacadeProperties properties) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumberGeneratorFacade(randomNumberGenerator, winningNumberValidator, winningNumbersRepository, numberReceiverFacade, properties);
    }

    WinningNumberGeneratorFacade createWinningNumberGeneratorFacadeForTest(RandomNumberGenerable numberGenerator, WinningNumbersRepository repository, NumberReceiverFacade numberReceiverFacade) {
        WinningFacadeProperties properties = WinningFacadeProperties.builder()
                .numberCount(6)
                .lowestNumber(1)
                .highestNumber(99)
                .build();
        return winningNumberGeneratorFacade(repository, numberReceiverFacade, numberGenerator, properties);
    }
}

