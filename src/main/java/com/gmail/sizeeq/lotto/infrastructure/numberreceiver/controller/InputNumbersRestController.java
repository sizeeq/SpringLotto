package com.gmail.sizeeq.lotto.infrastructure.numberreceiver.controller;

import com.gmail.sizeeq.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@Log4j2
@AllArgsConstructor
public class InputNumbersRestController {

    private final NumberReceiverFacade numberReceiverFacade;

    @PostMapping("/inputNumbers")
    public ResponseEntity<NumberReceiverResponseDto> inputNumbers(@RequestBody @Valid final InputNumbersRequestDto requestDto) {
        Set<Integer> numbersFromUser = new HashSet<>(requestDto.inputNumbers());
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(numbersFromUser);
        return ResponseEntity.ok(numberReceiverResponseDto);
    }
}
