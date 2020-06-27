package io.kpay.interview.service;

import io.kpay.interview.rest.dto.CreateRequestFlexMoney;
import io.kpay.interview.rest.dto.CreatedFlexMoney;
import io.kpay.interview.rest.dto.GetTakeMoney;
import io.kpay.interview.rest.dto.ShowFlexMoney;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class FlexMoneyServiceImplTest {

    Validator validator;
    @Autowired
    FlexMoneyService flexMoneyService;
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();


    }



    @DisplayName("뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다")
    @Test
    void 뿌리기 () {
        Set<ConstraintViolation<CreateRequestFlexMoney>> violations =validator.validate(CreateRequestFlexMoney.of(BigDecimal.TEN,0));
        Assertions.assertEquals(2,violations.size());
    }


    @DisplayName("뿌리기 시 발급된 token을 요청값으로 받습니다.")
    @Test
    void 받기(){
        final CreatedFlexMoney createdFlexMoney = flexMoneyService.create(1l, "test", BigDecimal.valueOf(100000), 10);

        final GetTakeMoney getTakeMoney = flexMoneyService.takeMoney(2l, "test", createdFlexMoney.getToken());

        Assertions.assertNotNull(getTakeMoney);
        Assertions.assertNotNull(getTakeMoney.getMoney());
        Assertions.assertTrue(getTakeMoney.getTakeId() > 0);

    }

    @DisplayName("token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다.")
    @Test
    void 조회(){
        final CreatedFlexMoney createdFlexMoney = flexMoneyService.create(1l, "test", BigDecimal.valueOf(100000), 10);

        final ShowFlexMoney showFlexMoney = flexMoneyService.show(1l, "test", createdFlexMoney.getToken());

        Assertions.assertAll("조회 ",
                ()-> assertNotNull(showFlexMoney.getCreateAt()),
                ()-> assertNotNull(showFlexMoney.getMoney()),
                ()-> assertNotNull(showFlexMoney.getTaken()),
                ()-> assertNotNull(showFlexMoney.getTaken())
        );

    }

}