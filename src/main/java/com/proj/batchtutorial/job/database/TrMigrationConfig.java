package com.proj.batchtutorial.job.database;

import com.proj.batchtutorial.core.domain.accounts.Accounts;
import com.proj.batchtutorial.core.domain.accounts.AccountsRepository;
import com.proj.batchtutorial.core.domain.orders.Orders;
import com.proj.batchtutorial.core.domain.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private OrdersRepository ordersRepository;

    @Autowired
    private AccountsRepository accountsRepository;

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
    public Step trMigrationStep(ItemReader trOrdersReader, ItemProcessor trOrderProcessor, ItemWriter trOrderWriter) {
        return stepBuilderFactory.get("trMigrationStep")
                .<Orders, Accounts>chunk(5) // 어떤 데이터 타입으로 읽어오고 쓸지, 5개씩 읽어서 commit(transaction)을 하겠다.
                .reader(trOrdersReader)
//                .writer(new ItemWriter() {
//                    @Override
//                    public void write(List items) throws Exception {
//                        items.forEach(System.out::println);
//                    }
//                })
                .processor(trOrderProcessor)
                .writer(trOrderWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Accounts> trOrderWriter() {
        return new RepositoryItemWriterBuilder<Accounts>()
                .repository(accountsRepository)
                .methodName("save")
                .build();
    }

    // itemWriter 구현 직접 repository save 구현 필요
    @Bean
    @StepScope
    public ItemWriter<Accounts> trOrderNormalWriter() {
        return new ItemWriter<Accounts>() {
            @Override
            public void write(List<? extends Accounts> items) throws Exception {
                items.forEach(item -> accountsRepository.save(item));
            }
        };
    }


    @Bean
    @StepScope
    public ItemProcessor<Orders, Accounts> trOrderProcessor() {
        return new ItemProcessor<Orders, Accounts>() {
            @Override
            public Accounts process(Orders item) throws Exception {
                return new Accounts(item);
            }
        };
    }

    @Bean
    @JobScope
    public RepositoryItemReader<Orders> trOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader") // 이름 지정
                .repository(ordersRepository) // 사용 repository
                .methodName("findAll") // 호출 메서드
                .pageSize(5) // 조회 데이터 사이즈 보통은 chunksize와 동일하게 설정
                .arguments(Arrays.asList()) // 직접 구현한 메서드 파라미터 입력
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC)) // 정렬 설정
                .build();
    }
}
