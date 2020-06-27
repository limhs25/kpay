package io.kpay.interview.repository;

import io.kpay.interview.domain.TakeUserMoney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeMoneyRepository extends JpaRepository<TakeUserMoney,Long> {
}
