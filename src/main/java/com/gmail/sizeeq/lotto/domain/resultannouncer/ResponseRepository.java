package com.gmail.sizeeq.lotto.domain.resultannouncer;

import java.util.Optional;

public interface ResponseRepository {

    ResultResponse save(ResultResponse response);

    boolean existsById(String hash);

    Optional<ResultResponse> findById(String hash);
}
