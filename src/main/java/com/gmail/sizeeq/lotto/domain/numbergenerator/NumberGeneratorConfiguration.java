package com.gmail.sizeeq.lotto.domain.numbergenerator;

import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;

public class NumberGeneratorConfiguration {

    WinningNumberGeneratorFacade createWinningNumberGeneratorFacadeForTest(RandomNumberGenerable numberGenerator, WinningNumbersRepository repository, NumberReceiverFacade numberReceiverFacade) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumberGeneratorFacade(winningNumberValidator, numberGenerator, repository, numberReceiverFacade);
    }
}

