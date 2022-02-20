package com.zs.aidata.core.config;

import com.zs.aidata.core.sys.mytest.job.SpringTransJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张顺
 * @since 2021/11/30
 */
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail jobDetail() {
        // 链式编程,可以携带多个参数,在Job类中声明属性 + setter方法
        return JobBuilder.newJob(SpringTransJob.class).withIdentity("springTransJob")
                .storeDurably().build();
    }

    @Bean
    public Trigger sampleJobTrigger() {
        // 每隔两秒执行一次
        SimpleScheduleBuilder scheduleBuilder =
                SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever();
        return TriggerBuilder.newTrigger().forJob(jobDetail()).withIdentity("sampleTrigger")
                .withSchedule(scheduleBuilder).build();
    }


}
