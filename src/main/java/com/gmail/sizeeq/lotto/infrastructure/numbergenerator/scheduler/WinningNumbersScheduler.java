package com.gmail.sizeeq.lotto.infrastructure.numbergenerator.scheduler;

import com.gmail.sizeeq.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WinningNumbersScheduler {

    private final WinningNumberGeneratorFacade winningNumberGeneratorFacade;

    @Scheduled(cron = "${lotto.number-generator.lotteryRunOccurrence}")
    public void generateWinningNumbers() {
        winningNumberGeneratorFacade.generateWinningNumbers();
    }
}
