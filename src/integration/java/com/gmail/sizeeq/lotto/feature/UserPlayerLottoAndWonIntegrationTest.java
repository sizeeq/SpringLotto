package com.gmail.sizeeq.lotto.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.gmail.sizeeq.lotto.BaseIntegrationTest;
import com.gmail.sizeeq.lotto.domain.numbergenerator.RandomNumberGenerable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class UserPlayerLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Autowired
    RandomNumberGenerable randomNumberGenerable;

    @Test
    public void user_should_win_and_system_should_generate_winners() {
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

        //when
        randomNumberGenerable.generateSixNumbers();
        //then

//    step 2: System generated winning numbers for draw date 19.11.2022 12:00
//    step 3: User made POST /inputNumbers with 6 numbers (1,2,3,4,5,6) at 16-11-2022 and system returned 200 (OK) ->
//            -> with message: "success" and Ticket (DrawDate: 19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)
//    step 4: 3 days and 1 minute passed, and it is 1 minute after draw date (19.11.2022, 12:01)
//    step 5: System fetched results for ticketId: sampleTicketId with draw date (19.11.2022 12:00, and saved it into database with 6 hits)
//    step 6: 3 hours passed, and it is 1 minuted after announcement time (19.11.2022, 15:01)
//    step 7: User made GET /results/sampleTicketId and system returned 200 (OK)
    }

}
