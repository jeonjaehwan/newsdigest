package com.newsdigest.newsdigest.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class FetchNewsJob extends QuartzJobBean {

    private final JobLauncher jobLauncher;
    private final Job fetchNewsJob;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("Starting FetchNewsJob at {}", new Date());
            jobLauncher.run(fetchNewsJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
        } catch (Exception e) {
            log.error("Failed to execute FetchNewsJob", e);
            throw new JobExecutionException(e);
        }
    }
}
