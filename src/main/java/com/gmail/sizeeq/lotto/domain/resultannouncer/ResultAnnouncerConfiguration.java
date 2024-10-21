package com.gmail.sizeeq.lotto.domain.resultannouncer;

import com.gmail.sizeeq.lotto.domain.resultchecker.ResultCheckerFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ResultAnnouncerConfiguration {

    @Bean
    ResultAnnouncerFacade resultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade, ResponseRepository repository, Clock clock) {
        return new ResultAnnouncerFacade(resultCheckerFacade, repository, clock);
    }
}
