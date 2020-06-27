package io.kpay.interview.rest.dto;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Value(staticConstructor = "of")
public class CreateRequestFlexMoney {

    @NotNull
    @Min(value = 1000,message = "최소 금액은 1000원 입니다.")
    private BigDecimal money;

    @NotNull
    @Min(value = 1,message = "최소 대상은 1 명 이상 입니다.")
    private Integer capacity;
}
