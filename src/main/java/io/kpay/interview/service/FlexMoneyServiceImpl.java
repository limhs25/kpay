package io.kpay.interview.service;

import io.kpay.interview.domain.FlexMoney;
import io.kpay.interview.domain.TakeUserMoney;
import io.kpay.interview.repository.FlexMoneyRepository;
import io.kpay.interview.repository.TakeMoneyRepository;
import io.kpay.interview.rest.dto.CreatedFlexMoney;
import io.kpay.interview.rest.dto.GetTakeMoney;
import io.kpay.interview.rest.dto.ShowFlexMoney;
import io.kpay.interview.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.stream.Collectors;

import static io.kpay.interview.domain.FlexConstants.TOKEN_EXPIRED_MIN;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlexMoneyServiceImpl implements FlexMoneyService{



    private final FlexMoneyRepository repository;
    private final TakeMoneyRepository takeMoneyRepository;

    private final RedisTemplate<String,String> redisTemplate;


    @Override
    public CreatedFlexMoney create(Long userId, String roomId, BigDecimal money, Integer available) {

        FlexMoney save = repository.saveAndFlush(FlexMoney.builder().userId(userId)
                .roomId(roomId)
                .money(money)
                .available(available)
                .expireDuration(Duration.ofMinutes(TOKEN_EXPIRED_MIN))
                .build());

        return CreatedFlexMoney.of(save.getToken());
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public GetTakeMoney takeMoney(Long userId, String roomId, String token) {
        FlexMoney moneyEntity = repository.findByToken(token).orElseThrow(()-> new ApiException("뿌리기 데이터가 없습니다."));
        TakeUserMoney save = takeMoneyRepository.save(moneyEntity.withdraw(userId,roomId));
        return GetTakeMoney.of(save.getMoney(),save.getId());

    }

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Override
    public ShowFlexMoney show(Long userId, String roomId, String token) {

        FlexMoney moneyEntity = repository.findByToken(token).orElseThrow(()-> new ApiException("뿌리기 데이터가 없습니다."));

        FlexMoney flexMoney = moneyEntity.currentStatus(userId);

        return ShowFlexMoney.builder()
                .createAt(flexMoney.getCreatedAt())
                .money(flexMoney.getMoney())
                .taken(flexMoney.getBalance())
                .takeUsers(flexMoney.takenMoneyInfo()
                        .stream()
                        .map(e-> ShowFlexMoney.TakeUser.builder()
                                .userId(e.getUserId())
                                .money(e.getMoney()).build()).collect(Collectors.toList())).build();

    }



}
