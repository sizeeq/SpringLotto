package com.gmail.sizeeq.lotto.apivalidationerror;

import com.gmail.sizeeq.lotto.BaseIntegrationTest;
import com.gmail.sizeeq.lotto.infrastructure.apivalidation.ApiValidationErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_has_empty_input_numbers() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": []
                        }
                        """)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(result.messages()).containsExactly("inputNumbers must not be empty");
    }

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_does_not_have_input_numbers() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {}
                        """)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "inputNumbers must not be null",
                "inputNumbers must not be empty"
        );
    }
}
