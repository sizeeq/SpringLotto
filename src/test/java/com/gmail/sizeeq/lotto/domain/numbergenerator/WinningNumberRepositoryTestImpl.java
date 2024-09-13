package com.gmail.sizeeq.lotto.domain.numbergenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class WinningNumberRepositoryTestImpl implements WinningNumbersRepository {

    private final Map<LocalDateTime, WinningNumbers> winningNumbersDatabase = new ConcurrentHashMap<>();

    @Override
    public Optional<WinningNumbers> findNumbersByDate(LocalDateTime date) {
        return Optional.ofNullable(winningNumbersDatabase.get(date));
    }

    @Override
    public boolean existsByDate(LocalDateTime nextDrawDate) {
        return winningNumbersDatabase.get(nextDrawDate) != null;
    }

    @Override
    public WinningNumbers save(WinningNumbers winningNumbers) {
        return winningNumbersDatabase.put(winningNumbers.date(), winningNumbers);
    }
}
