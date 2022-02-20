package com.zs.aidata.core.sys.mytest.job;

import cn.hutool.extra.spring.SpringUtil;
import com.zs.aidata.core.sys.mytest.service.ISpringTransTestService;
import com.zs.aidata.core.sys.mytest.service.SpringTransTestService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.inject.Inject;

/**
 * @author 张顺
 * @since 2021/11/30
 */
@Slf4j
@Component
public class SpringTransJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("=============SpringTransJob=====开始==============");
        SpringUtil.getBean(ISpringTransTestService.class).processMqs();
        log.info("=============SpringTransJob=====结束==============");
    }
}
