package io.kpay.interview.support.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
}