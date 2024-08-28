package com.newsdigest.newsdigest.config;

import com.newsdigest.newsdigest.job.FetchNewsJob;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(FetchNewsJob.class);
        factoryBean.setDescription("Invoke Fetch News Job service to fetch and save news articles from external API every hour.");
        factoryBean.setDurability(true);

        return factoryBean;
    }

    @Bean
    public CronTriggerFactoryBean triggerFactoryBean(JobDetail jobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression("0 30 18 * * ?");
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return factoryBean;
    }
}
