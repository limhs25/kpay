package io.kpay.interview.domain;


import io.kpay.interview.support.exception.ApiException;
import io.kpay.interview.support.utils.TokenGenerator;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.kpay.interview.domain.FlexConstants.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder(toBuilder = true)
@Getter
@ToString
@Table(name = "tb_flex_money", uniqueConstraints = @UniqueConstraint(columnNames = {"token"}))
@EntityListeners(AuditingEntityListener.class)
public class FlexMoney {


    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private String roomId;
    @Column(length = 3, unique = true)
    private String token;

    private BigDecimal money;
    private BigDecimal balance;

    private Integer available;

    @Builder.Default
    @OneToMany(mappedBy = "flexMoney",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TakeUserMoney> takeMoneys = new ArrayList<>();



    private Duration expireDuration;
    @CreatedDate
    private LocalDateTime createdAt;


    @Version
    private Integer version;



    @PrePersist
    public void prePersist() {

        if(StringUtils.isEmpty(this.token)){
            this.token = TokenGenerator.create(TOKEN_DIGIT);
        }

        this.balance = this.money;
        if(Objects.isNull(expireDuration))
            this.expireDuration = Duration.ofMinutes(TOKEN_EXPIRED_MIN);
        AtomicReference<BigDecimal> reference = new AtomicReference<>(this.balance);

        final BigDecimal divide = this.balance.divide(BigDecimal.valueOf(available), BigDecimal.ROUND_DOWN);

        this.takeMoneys = IntStream.range(0,available).mapToObj(idx -> {
            BigDecimal amount ;
            if((available-idx) ==1 ){
                amount = reference.get();
            }else{
                amount = divide;
            }
            reference.set(reference.get().subtract(amount));
            return TakeUserMoney.builder().flexMoney(this)
                    .money(amount)
                    .build();
        })
                .collect(Collectors.toList());



    }

    @PreUpdate
    protected void preUpdate(){
        BigDecimal takenMoney = this.getTakeMoneys().stream().filter(e -> Objects.nonNull(e.getUserId()) && Objects.nonNull(e.getMoney()))
                .map(TakeUserMoney::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.balance = this.balance.subtract(takenMoney);

    }



    public TakeUserMoney withdraw(Long userId, String roomId){
        if(this.userId.equals(userId)){
            throw new ApiException("뿌린 사람은 받을 수 없습니다.");
        }
        if(!this.roomId.equals(roomId)){
            throw new ApiException("뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.");
        }
        if(this.createdAt.plus(this.expireDuration).isBefore(LocalDateTime.now())){
            throw new ApiException("뿌린 건은 10분간만 유효합니다.");
        }
        if(this.takeMoneys.stream()
                .anyMatch(e -> userId.equals(e.getUserId()))) {
            throw  new ApiException("뿌리기 당 한 사용자는 한번만 받을 수 있습니다.");
        }
        BigDecimal remainingMoney = checkBalance();
        if(remainingMoney.equals(BigDecimal.ZERO)){
            throw  new ApiException("남아 있는 금액이 없습니다.");
        }
        return this.takeMoneys.stream()
                .filter(e -> Objects.isNull(e.getUserId()))
                .findFirst()
                .orElseThrow(() -> new ApiException("너무 늦었어요!!!"))
                .toBuilder().userId(userId).build();
    }

    protected BigDecimal checkBalance(){
        BigDecimal takenMoney = this.takeMoneys
                .stream().filter(e -> Objects.nonNull(e.getUserId()) && Objects.nonNull(e.getMoney()))
                .map(TakeUserMoney::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
       return this.balance.subtract(takenMoney);
    }


    public FlexMoney currentStatus(Long userId){
        //뿌린 사람 자신만 조회를 할 수 있습니다.
        if(!this.userId.equals(userId)){
            throw new ApiException("뿌린 사람 자신만 조회를 할 수 있습니다.");
        }
        if(Period.between(this.createdAt.toLocalDate(),LocalDateTime.now().toLocalDate())
                .getDays() > HISTORY_EXPIRED_DAY){
            throw new ApiException("뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.");
        }

        return this;

    }

    public List<TakeUserMoney> takenMoneyInfo(){
        return this.takeMoneys.stream()
                .filter(e -> Objects.nonNull(e.getUserId()) && Objects.nonNull(e.getMoney()))
                .collect(Collectors.toList());
    }





}
