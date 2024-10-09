package com.gmail.sizeeq.lotto.domain.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class NumberReceiverConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    HashGenerable hashGenerable() {
        return new HashGenerator();
    }

    @Bean
    NumberReceiverFacade numberReceiverFacade(HashGenerable hashGenerator, Clock clock, TicketRepository ticketRepository) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator dateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(numberValidator, dateGenerator, hashGenerator, ticketRepository);
    }

    NumberReceiverFacade createNumberReceiverFacadeForTest(HashGenerable hashGenerator, Clock clock, TicketRepository ticketRepository) {
        return numberReceiverFacade(hashGenerator, clock, ticketRepository);
    }
}
