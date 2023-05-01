package com.proj.batchtutorial;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * packageName   : com.proj.batchtutorial.job.hello
 * fileName      : SpringBatchTestConfig
 * author        : kang_jungwoo
 * date          : 2023/05/01
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/05/01       kang_jungwoo         최초 생성
 */
@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class SpringBatchTestConfig {
}
