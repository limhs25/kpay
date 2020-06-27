package io.kpay.interview.support.security;

import lombok.*;

@Value(staticConstructor = "of")
public class UserRoom {
    private final Long userId;
    private final String roomId;
}
