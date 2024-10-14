package com.gmail.sizeeq.lotto.infrastructure.numbergenerator.scheduler;

import com.gmail.sizeeq.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class WinningNumbersScheduler {

    private final WinningNumberGeneratorFacade winningNumberGeneratorFacade;

    @Scheduled(cron = "${lotto.number-generator.lotteryRunOccurrence}")
    public WinningNumbersDto generateWinningNumbers() {
        log.info("Scheduled generating winning numbers");
        WinningNumbersDto winningNumbersDto = winningNumberGeneratorFacade.generateWinningNumbers();
        log.info("Generated winning numbers: {}", winningNumbersDto.getWinningNumbers());
        log.info("Generated date: {}", winningNumbersDto.getDate());
        return winningNumbersDto;
    }
}
