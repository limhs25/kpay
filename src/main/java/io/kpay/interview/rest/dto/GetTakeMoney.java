package io.kpay.interview.rest.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class GetTakeMoney {

    private BigDecimal money;
    private Long takeId;

}
