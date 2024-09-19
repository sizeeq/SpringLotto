package com.gmail.sizeeq.lotto.domain.resultannouncer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseRepositoryTestImpl implements ResponseRepository {

    private final Map<String, ResultResponse> responseRepositoryDb = new ConcurrentHashMap<>();

    @Override
    public ResultResponse save(ResultResponse response) {
        return responseRepositoryDb.put(response.hash(), response);
    }

    @Override
    public boolean existsById(String hash) {
        return responseRepositoryDb.containsKey(hash);
    }

    @Override
    public Optional<ResultResponse> findById(String hash) {
        return Optional.ofNullable(responseRepositoryDb.get(hash));
    }
}
