package com.newsdigest.newsdigest.config;

import com.newsdigest.newsdigest.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final NewsService newsService;

    @Bean
    public Job newsFetchJob() {
        return new JobBuilder("fetchNewsJob", jobRepository)
                .start(newsFetchStep())
                .build();
    }

    @Bean
    public Step newsFetchStep() {
        return new StepBuilder("fetchNewsStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    newsService.fetchAndSaveNews();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
