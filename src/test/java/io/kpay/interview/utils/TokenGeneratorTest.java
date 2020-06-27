package io.kpay.interview.utils;


import io.kpay.interview.support.utils.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static io.kpay.interview.domain.FlexConstants.TOKEN_DIGIT;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class TokenGeneratorTest {

    @DisplayName("token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.")
    @Test
    public void 토큰_생성(){
        String token = TokenGenerator.create(3);
        assertThat(token.length())
                .isEqualTo(TOKEN_DIGIT);

    }

}
