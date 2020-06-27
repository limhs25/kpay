package io.kpay.interview.rest.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/*

뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은
사용자 아이디] 리스트
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ShowFlexMoney {
    private LocalDateTime createAt;
    private BigDecimal money;
    private BigDecimal taken;

    private List<TakeUser> takeUsers;


    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class TakeUser {
        private Long userId;
        private BigDecimal money;
    }

}
