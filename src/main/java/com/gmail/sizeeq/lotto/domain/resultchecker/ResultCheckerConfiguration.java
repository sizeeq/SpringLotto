package com.gmail.sizeeq.lotto.domain.resultchecker;

import com.gmail.sizeeq.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;

public class ResultCheckerConfiguration {

    ResultCheckerFacade createResultCheckerFacadeForTests(WinningNumberGeneratorFacade winningNumberGeneratorFacade, NumberReceiverFacade numberReceiverFacade, PlayerRepository playerRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        return new ResultCheckerFacade(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository, winnersRetriever);
    }
}
