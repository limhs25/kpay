package io.kpay.interview.domain;

import io.kpay.interview.repository.FlexMoneyRepository;
import io.kpay.interview.repository.TakeMoneyRepository;
import io.kpay.interview.service.FlexMoneyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LockTest {
    @Autowired
    private FlexMoneyService service;
    @Autowired
    private FlexMoneyRepository repository;

    @Autowired
    private TakeMoneyRepository takeMoneyRepository;

    @Before
    public void setUp() throws Exception {

        repository.save(FlexMoney.builder().userId(1l)
                .roomId("1")
                .money(BigDecimal.valueOf(100000))
                .available(5)
                .expireDuration(Duration.ofMinutes(3))
                .build());

    }

    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void DB_LOCK_TEST() throws Throwable {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(() -> {
            System.out.println("Thread 1");
            FlexMoney entity = repository.findById(1l).get();
            FlexMoney toUpdate = entity.toBuilder().money(BigDecimal.valueOf(5000)).build();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            repository.save(toUpdate);

        });

        executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println(" Thread 2");
                FlexMoney entity = repository.findById(1l).get();
                FlexMoney toUpdate = entity.toBuilder().money(BigDecimal.valueOf(2000)).build();
                repository.save(toUpdate);
            }
        });
        try {
            future1.get();
        } catch (ExecutionException e) {
            System.out.println("Exception from thread " + e.getCause().getClass());
            throw e.getCause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

    }


    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void 같은_데이터_업데이트하는경우_예외() {
            FlexMoney flexMoney = repository.save(FlexMoney.builder().userId(1l)
                    .roomId("1")
                    .money(BigDecimal.valueOf(100000))
                    .available(5)
                    .expireDuration(Duration.ofMinutes(3))
                    .build());

            takeMoneyRepository.saveAll(Arrays.asList(
                    TakeUserMoney.builder().id(10l).flexMoney(flexMoney).build().withVersion(1),
                    TakeUserMoney.builder().id(10l).flexMoney(flexMoney).build().withVersion(2)
                    )
            );
    }
}