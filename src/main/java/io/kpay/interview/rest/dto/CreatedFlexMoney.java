package io.kpay.interview.rest.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class CreatedFlexMoney {
    private final String token;
}
