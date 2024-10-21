package com.gmail.sizeeq.lotto.domain.resultchecker;

import com.gmail.sizeeq.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResultCheckerConfiguration {

    @Bean
    ResultCheckerFacade resultCheckerFacade(WinningNumberGeneratorFacade winningNumberGeneratorFacade, NumberReceiverFacade numberReceiverFacade, PlayerRepository playerRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        return new ResultCheckerFacade(winningNumberGeneratorFacade, numberReceiverFacade, playerRepository, winnersRetriever);
    }
}
