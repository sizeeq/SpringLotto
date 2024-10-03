package com.gmail.sizeeq.lotto.infrastructure.numbergenerator.http;

import com.gmail.sizeeq.lotto.domain.numbergenerator.RandomNumberGenerable;
import com.gmail.sizeeq.lotto.domain.numbergenerator.WinningFacadeProperties;
import com.gmail.sizeeq.lotto.domain.numbergenerator.dto.SixRandomNumbersDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;
    private final WinningFacadeProperties properties;

    @Override
    public SixRandomNumbersDto generateSixNumbers(int count, int lowerBound, int upperBound) {
        log.info("Fetching numbers from external http server...");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        try {
            final ResponseEntity<List<Integer>> response = makeGetRequest(count, lowerBound, upperBound, requestEntity);
            Set<Integer> sixDistinctNumbers = getSixRandomDistinctNumbers(response);
            if (sixDistinctNumbers.size() != properties.numberCount()) {
                log.error("Set is less than: {} Have to request one more time", count);
                return generateSixNumbers(count, lowerBound, upperBound);
            }
            return SixRandomNumbersDto.builder()
                    .numbers(sixDistinctNumbers)
                    .build();
        } catch (ResourceAccessException e) {
            log.error("Error while fetching numbers from external http server " + e.getMessage());
            return SixRandomNumbersDto.builder().build();
        }
    }

    private ResponseEntity<List<Integer>> makeGetRequest(int count, int lowerBound, int upperBound, HttpEntity<HttpHeaders> requestEntity) {
        String urlForService = getUrlForService("/api/v1.0/random");
        final String url = UriComponentsBuilder.fromHttpUrl(urlForService)
                .queryParam("min", lowerBound)
                .queryParam("max", upperBound)
                .queryParam("count", count)
                .toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    private Set<Integer> getSixRandomDistinctNumbers(ResponseEntity<List<Integer>> response) {
        List<Integer> body = response.getBody();
        if (body == null) {
            log.error("Response body was null returning empty collection.");
            return Collections.emptySet();
        }
        log.info("Successful response: {}", response);
        HashSet<Integer> distinctNumbers = new HashSet<>(body);

        return distinctNumbers.stream()
                .limit(properties.numberCount())
                .collect(Collectors.toSet());
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
