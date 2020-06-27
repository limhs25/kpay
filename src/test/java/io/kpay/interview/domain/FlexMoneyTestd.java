package io.kpay.interview.domain;

import io.kpay.interview.support.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
class FlexMoneyTestd {
    FlexMoney flexMoney;
    @BeforeEach
    public void before(){
        flexMoney = FlexMoney.builder()
                .roomId("test")
                .money(BigDecimal.TEN).userId(1l).available(7)
                .expireDuration(Duration.ofMinutes(10))
                .createdAt(LocalDateTime.now().minusWeeks(2))
                .build();

        flexMoney.prePersist();
    }


    @DisplayName("뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기\n" +
            "실패 응답이 내려가야 합니다.")
    @Test
    void 뿌리기_이후_10분_이후는_돈을_받을수없다() {

        Assertions.assertThrows(ApiException.class , () -> {
            flexMoney.withdraw(2l,"test");
        });
    }

    @DisplayName("token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다")
    @Test
    void token은_3자리_문자열로__구성 (){
        Assertions.assertSame(3, flexMoney.getToken().length());
    }

    @DisplayName("뿌릴 금액을 인원수에 맞게 분배하여 저장합니다")
    @Test
    void 뿌릴_금액을_인원수에_맞게_분배하여_저장합니다(){
        Assertions.assertSame(flexMoney.getAvailable(), flexMoney.getTakeMoneys().size());

        Assertions.assertSame(flexMoney.getMoney(),
                flexMoney.getTakeMoneys().stream().map(TakeUserMoney::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
    }



    @DisplayName("뿌리기 당 한 사용자는 한번만 받을 수 있습니다.")
    @Test
    void 뿌리기_당_한_사용자는_한번만_받을_수_있습니다(){
        Assertions.assertThrows(ApiException.class, ()-> {
            final TakeUserMoney withdraw = flexMoney.withdraw(3l,"test");
            final TakeUserMoney withdraw2 = flexMoney.withdraw(3l,"test");
        });
    }

    @DisplayName("자신이 뿌리기한 건은 자신이 받을 수 없습니다.")
    @Test
    void 자신이_뿌리기한_건은_자신이_받을_수_없습니다(){
        Assertions.assertThrows(ApiException.class, ()-> {
            final TakeUserMoney withdraw = flexMoney.withdraw(1l,"test");
        });
    }



    @DisplayName("뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.")
    @Test
    void 뿌린기가_호출된_대화방과_동일한_대화방에_속한_사용자만이_받을_수_있습니다(){
        Assertions.assertThrows(ApiException.class, ()-> {
            final TakeUserMoney withdraw = flexMoney.withdraw(5l,"afdsafasfa");
        });
    }





    @DisplayName("뿌린 사람 자신만 조회를 할 수 있습니다.")
    @Test
    void 뿌린_사람_자신만_조회를_할_수_있습니다(){
        Assertions.assertThrows(ApiException.class, ()-> {
            flexMoney.currentStatus(2l);
        });
    }


    @DisplayName("뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.")
    @Test
    void 뿌린_건에_대한_조회는_7일_동안_할_수_있습니다(){
        Assertions.assertThrows(ApiException.class, ()-> {
            flexMoney.currentStatus(1l);
        });
    }




}