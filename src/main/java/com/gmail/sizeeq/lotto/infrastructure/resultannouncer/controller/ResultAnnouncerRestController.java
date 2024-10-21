package com.gmail.sizeeq.lotto.infrastructure.resultannouncer.controller;

import com.gmail.sizeeq.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import com.gmail.sizeeq.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@AllArgsConstructor
public class ResultAnnouncerRestController {

    ResultAnnouncerFacade resultAnnouncerFacade;

    @GetMapping("/results/{id}")
    public ResponseEntity<ResultAnnouncerResponseDto> getResultById(@PathVariable String id) {
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(id);
        return ResponseEntity.ok(resultAnnouncerResponseDto);
    }
}
