package com.gmail.sizeeq.lotto.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.gmail.sizeeq.lotto.BaseIntegrationTest;
import com.gmail.sizeeq.lotto.domain.numbergenerator.RandomNumberGenerable;
import com.gmail.sizeeq.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.gmail.sizeeq.lotto.domain.numbergenerator.exception.WinningNumbersNotFoundException;
import com.gmail.sizeeq.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserPlayerLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Autowired
    RandomNumberGenerable randomNumberGenerable;

    @Autowired
    WinningNumberGeneratorFacade winningNumberGeneratorFacade;

    @Test
    public void user_should_win_and_system_should_generate_winners() throws Exception {

//    step 1: External server returns six random numbers (1,2,3,4,5,6)
        //given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/random?min=1&max=99&count=25")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [1, 2, 3, 4, 5, 6, 82, 82, 83, 83, 86, 57, 10, 81, 53, 93, 50, 54, 31, 88, 15, 43, 79, 32, 43]
                                """.trim()
                        )));

//    step 2: System fetched winning numbers for draw date 19.11.2022 12:00
        // given
        LocalDateTime drawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        // when && then
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                            try {
                                return !winningNumberGeneratorFacade.retrieveWinningNumbersByDate(drawDate).getWinningNumbers().isEmpty();
                            } catch (WinningNumbersNotFoundException e) {
                                return false;
                            }
                        }
                );
//    step 3: User made POST /inputNumbers with 6 numbers (1,2,3,4,5,6) at 16-11-2022 and system returned 200 (OK) ->
//            -> with message: "success" and Ticket (DrawDate: 19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)
        //given
        //when
        ResultActions performPostInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": [1,2,3,4,5,6]
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        MvcResult mvcResult = performPostInputNumbers.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        NumberReceiverResponseDto numberReceiverResponseDto = objectMapper.readValue(json, NumberReceiverResponseDto.class);

        assertAll(
                () -> assertThat(numberReceiverResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(numberReceiverResponseDto.ticketDto().hash()).isNotNull(),
                () -> assertThat(numberReceiverResponseDto.message()).isEqualTo("Success!")
        );


//    step 4: User made GET /results/notExistingId and system returned 404(NOT FOUND) and body with (message: Not found for id notExistingId and status NOT_FOUND)
        //given
        //when
        ResultActions performResultWithNotExistingID = mockMvc.perform(get("/results/" + "notExistingId"));
        //then
        performResultWithNotExistingID.andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                                {
                                "message": "Not found for id: notExistingId",
                                "status": "NOT_FOUND"
                                }
                                """.trim()
                ));

//    step 5: 3 days and 1 minute passed, and it is 1 minute after draw date (19.11.2022, 12:01)
//    step 6: System fetched results for ticketId: sampleTicketId with draw date (19.11.2022 12:00, and saved it into database with 6 hits)
//    step 7: 3 hours passed, and it is 1 minuted after announcement time (19.11.2022, 15:01)
//    step 8: User made GET /results/sampleTicketId and system returned 200 (OK)
    }

}
