package com.gmail.sizeeq.lotto;

import com.gmail.sizeeq.lotto.domain.numbergenerator.WinningFacadeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({WinningFacadeProperties.class})
public class LottoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LottoApplication.class, args);
    }
}
