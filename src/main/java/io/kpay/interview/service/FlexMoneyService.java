package io.kpay.interview.service;

import io.kpay.interview.rest.dto.CreatedFlexMoney;
import io.kpay.interview.rest.dto.GetTakeMoney;
import io.kpay.interview.rest.dto.ShowFlexMoney;

import java.math.BigDecimal;

public interface FlexMoneyService {


    CreatedFlexMoney create(Long userId, String roomId, BigDecimal money, Integer receiptCnt);

    GetTakeMoney takeMoney(Long userId, String roomId, String token);

    ShowFlexMoney show(Long userId, String roomId, String token);

//    ShareMoney createShareMoney(Long userId, Long roomId, Long amount, Integer splitSize);
//
//    ShareMoney showMeTheMoney(Long userId, Long roomId, String token);
//
//    ShareMoney findShareMoney(String token);
}
