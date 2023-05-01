package com.proj.batchtutorial.job.filedata;

import com.proj.batchtutorial.job.filedata.dto.Player;
import com.proj.batchtutorial.job.filedata.dto.PlayerYears;
import com.proj.batchtutorial.job.filedata.mapper.PlayerFieldSetMapper;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

/**
 * packageName   : com.proj.batchtutorial.job.filedata
 * fileName      : FileDataReadWriteConfig
 * author        : kang_jungwoo
 * date          : 2023/05/01
 * description   : 파일 데이터 읽고 쓰기
 * run           : --spring.batch.job.names=fileReadWriteJob
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/05/01       kang_jungwoo         최초 생성
 */
@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fileReadWriteJob(Step fileReadWriteStep) {
        return jobBuilderFactory.get("fileReadWriteJob")
                .incrementer(new RunIdIncrementer())
                .start(fileReadWriteStep)
                .build();
    }

    @Bean
    @JobScope
    public Step fileReadWriteStep(ItemReader playerItemReader
            ,ItemProcessor playerItemProcessor
            ,FlatFileItemWriter playerItemWriter) {
        return stepBuilderFactory.get("fileReadWriteStep")
                .<Player, PlayerYears>chunk(5) // read 할 dataType, write 할 dataType
                .reader(playerItemReader)
//                .writer(new ItemWriter() {
//                    @Override
//                    public void write(List items) throws Exception {
//                        items.forEach(System.out::println);
//                    }
//                })
                .processor(playerItemProcessor)
                .writer(playerItemWriter)
                .build();
    }


    @Bean
    @StepScope
    public ItemProcessor<Player, PlayerYears> playerItemProcessor() {
        return new ItemProcessor<Player, PlayerYears>() {
            @Override
            public PlayerYears process(Player item) throws Exception {
                return new PlayerYears(item);
            }
        };
    }


    @Bean
    @StepScope
    public FlatFileItemReader<Player> playerItemReader() {
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerItemReader")
                .resource(new FileSystemResource("players.csv"))
                .lineTokenizer(new DelimitedLineTokenizer())
                .fieldSetMapper(new PlayerFieldSetMapper()) // 읽어온 데이터를 객체로 매핑
                .linesToSkip(1) // 첫번째 줄은 스킵
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<PlayerYears> playerItemWriter() {
        // 어떤 필드를 사용할지 명시
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"ID", "lastName", "position", "yearsExperience"});
        fieldExtractor.afterPropertiesSet();

        // 어떤 기준으로 파일을 생성하는지
        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(","); // 구분자
        lineAggregator.setFieldExtractor(fieldExtractor);

        // 생성할 파일
        FileSystemResource outputResource = new FileSystemResource("players_output.txt");

        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }
}
