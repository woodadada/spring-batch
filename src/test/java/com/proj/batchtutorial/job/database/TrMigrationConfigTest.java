package com.proj.batchtutorial.job.database;

import com.proj.batchtutorial.SpringBatchTestConfig;
import com.proj.batchtutorial.core.domain.accounts.AccountsRepository;
import com.proj.batchtutorial.core.domain.orders.Orders;
import com.proj.batchtutorial.core.domain.orders.OrdersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * packageName   : com.proj.batchtutorial.job.database
 * fileName      : TrMigrationConfigTest
 * author        : kang_jungwoo
 * date          : 2023/05/01
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/05/01       kang_jungwoo         최초 생성
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigrationConfig.class}) // 환경 설정을 위한 추가 testConfig / testTargetConfigClass
class TrMigrationConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @AfterEach
    public void cleanUpEach() {
        ordersRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test
    public void success_noData() throws Exception {
        // when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0, accountsRepository.count());
    }

    @Test
    public void success_existData() throws Exception {
        // given
        Orders orders1 = new Orders(null, "kakao gift", 15000, new Date());
        Orders orders2 = new Orders(null, "naver gift", 15000, new Date());

        ordersRepository.save(orders1);
        ordersRepository.save(orders2);

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(2, accountsRepository.count());
    }

}