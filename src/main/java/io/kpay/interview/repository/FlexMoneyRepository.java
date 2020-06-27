package io.kpay.interview.repository;

import io.kpay.interview.domain.FlexMoney;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlexMoneyRepository extends JpaRepository<FlexMoney,Long> {

    Optional<FlexMoney> findByToken(String token);
}
