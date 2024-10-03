package com.gmail.sizeeq.lotto.domain.numbergenerator;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "lotto.number-generator.facade")
@Builder
public record WinningFacadeProperties(
        int numberCount,
        int lowestNumber,
        int highestNumber
) {
}
