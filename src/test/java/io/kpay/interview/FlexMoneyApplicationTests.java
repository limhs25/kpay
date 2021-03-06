package io.kpay.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kpay.interview.rest.dto.CreateRequestFlexMoney;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FlexMoneyApplicationTests {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("t○ 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.\n" +
            "○ 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.")
    @Test
    public void 뿌리기_API() throws Throwable, Exception {

        final CreateRequestFlexMoney request = CreateRequestFlexMoney.of(BigDecimal.valueOf(10000), 10);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-USER-ID", String.valueOf(1));
        httpHeaders.add("X-ROOM-ID", "1");

        final MvcResult result = mockMvc.perform(post("/api/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .headers(httpHeaders)
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("token").exists())
                .andReturn();


    }

}
