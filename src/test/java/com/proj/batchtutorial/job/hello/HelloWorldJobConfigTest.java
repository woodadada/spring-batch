package com.proj.batchtutorial.job.hello;

import com.proj.batchtutorial.SpringBatchTestConfig;
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

/**
 * packageName   : com.proj.batchtutorial.job.hello
 * fileName      : HelloWorldJobConfigTest
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
@SpringBootTest(classes = {SpringBatchTestConfig.class, HelloWorldJobConfig.class}) // 환경 설정을 위한 추가 testConfig / testTargetConfigClass
class HelloWorldJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void success() throws Exception {
        // when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
    }

}