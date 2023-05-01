package com.proj.batchtutorial.job.database;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName   : com.proj.batchtutorial.job.database
 * fileName      : TrMigrationConfig
 * author        : kang_jungwoo
 * date          : 2023/05/01
 * description   : 주문 테이블 -> 정산 테이블 데이터 이관
 * run           : --spring.batch.job.names=trMigrationJob
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/05/01       kang_jungwoo         최초 생성
 */
@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @Bean
    @JobScope
    public Step trMigrationStep() {
        return stepBuilderFactory.get("trMigrationStep")
                .build();
    }
}
