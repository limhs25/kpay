package io.kpay.interview.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
@Builder(toBuilder = true)
@Getter
@Table(name = "tb_take_money")
@EntityListeners(AuditingEntityListener.class)
@With
public class TakeUserMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flexMoney_id")
    private FlexMoney flexMoney;

    @Column
    private BigDecimal money;

    @Column
    private Long userId;

    @Version
    private Integer version;

}
