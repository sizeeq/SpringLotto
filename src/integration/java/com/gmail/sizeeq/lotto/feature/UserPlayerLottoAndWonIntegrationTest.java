package com.gmail.sizeeq.lotto.feature;

import com.gmail.sizeeq.lotto.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

public class UserPlayerLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Test
    public void user_should_win_and_system_should_generate_winners() {
//    step 1: User made POST /inputNumbers with 6 numbers (1,2,3,4,5,6) at 16-11-2022 and system returned 200 (OK) ->
//    -> with message: "success" and Ticket (DrawDate: 19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)



//    step 2: System generated winning numbers for draw date 19.11.2022 12:00
//    step 3: 3 days and 1 minute passed, and it is 1 minute after draw date (19.11.2022, 12:01)
//    step 4: System generated results for ticketId: sampleTicketId with draw date (19.11.2022 12:00, and saved it into database with 6 hits)
//    step 5: 3 hours passed, and it is 1 minuted after announcement time (19.11.2022, 15:01)
//    step 6: User made GET /results/sampleTicketId and system returned 200 (OK)
    }

}
