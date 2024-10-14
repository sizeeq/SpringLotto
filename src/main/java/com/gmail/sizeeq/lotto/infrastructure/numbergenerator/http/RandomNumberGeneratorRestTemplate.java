package com.gmail.sizeeq.lotto.infrastructure.numbergenerator.http;

import com.gmail.sizeeq.lotto.domain.numbergenerator.RandomNumberGenerable;
import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log4j2
public class RandomNumberGeneratorRestTemplate implements RandomNumberGenerable {

    public static final int WINNING_NUMBER_COUNT = 6;
    public static final String RANDOM_NUMBER_SERVICE_PATH = "/api/v1.0/random";

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers(int count, int lowerBand, int upperBand) {
        log.info("Started fetching winning numbers: count={}, lowerBand={}, upperBand={}", count, lowerBand, upperBand);
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        try {
            final ResponseEntity<List<Integer>> response = makeGetRequest(count, lowerBand, upperBand, requestEntity);
            Set<Integer> sixDistinctNumbers = getSixRandomDistinctNumbers(response);
            if (sixDistinctNumbers.size() != WINNING_NUMBER_COUNT) {
                log.warn("Set size is less than expected. Retrying...");
                return generateSixRandomNumbers(count, lowerBand, upperBand);
            }
            return SixRandomNumbersDto.builder()
                    .numbers(sixDistinctNumbers)
                    .build();
        } catch (ResourceAccessException e) {
            log.error("Error fetching numbers: {}", e.getMessage(), e);
            return SixRandomNumbersDto.builder().build();
        }
    }

    private ResponseEntity<List<Integer>> makeGetRequest(int count, int lowerBand, int upperBand, HttpEntity<HttpHeaders> requestEntity) {
        final String url = UriComponentsBuilder.fromHttpUrl(getUrlForService(RANDOM_NUMBER_SERVICE_PATH))
                .queryParam("min", lowerBand)
                .queryParam("max", upperBand)
                .queryParam("count", count)
                .toUriString();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
    }

    private Set<Integer> getSixRandomDistinctNumbers(ResponseEntity<List<Integer>> response) {
        List<Integer> numbers = response.getBody();
        if (numbers == null) {
            log.error("Response body was null, returning empty");
            return Collections.emptySet();
        }
        log.info("Success Response Body Returned: {}", response);
        Set<Integer> distinctNumbers = new HashSet<>(numbers);
        return distinctNumbers.stream()
                .limit(WINNING_NUMBER_COUNT)
                .collect(Collectors.toSet());
    }


    private String getUrlForService(String service) {
        if (port > 0) {
            return String.format("%s:%d%s", uri, port, service);
        }
        return uri + service;
    }
}