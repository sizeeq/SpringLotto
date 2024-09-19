package com.gmail.sizeeq.lotto.domain.resultannouncer;

import com.gmail.sizeeq.lotto.domain.resultchecker.ResultCheckerFacade;

import java.time.Clock;

public class ResultAnnouncerConfiguration {

    ResultAnnouncerFacade createResultAnnouncerFacadeForTests(ResultCheckerFacade resultCheckerFacade, ResponseRepository repository, Clock clock) {
        return new ResultAnnouncerFacade(resultCheckerFacade, repository, clock);
    }
}
