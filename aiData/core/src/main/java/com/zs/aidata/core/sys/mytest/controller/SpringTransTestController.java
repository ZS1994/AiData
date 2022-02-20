package com.zs.aidata.core.sys.mytest.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.zs.aidata.core.sys.mytest.service.ISpringTransTestService;
import com.zs.aidata.core.tools.QuartzSchedulerFactoryBean2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * spring事务测试
 *
 * @author 张顺
 * @since 2021/11/30
 */
@Slf4j
@Api(tags = {"spring事务测试"})
@RestController
@RequestMapping(value = "core/springTransTestController", headers = "Accept=application/json", produces =
        "application/json;charset=UTF-8")
public class SpringTransTestController {

    @Inject
    private ISpringTransTestService iSpringTransTestService;

    @PostMapping("processJob")
    @ApiOperation(value = "模拟JOB无事务", notes = "模拟JOB无事务")
    public void processJob() {
        iSpringTransTestService.processJob();
    }

    @PostMapping("processJob2")
    @ApiOperation(value = "模拟JOB使用注解事务", notes = "模拟JOB使用注解事务")
    public void processJob2() {
        iSpringTransTestService.processJobWtihTransAni();
    }

    @PostMapping("processJob2_1")
    @ApiOperation(value = "模拟JOB使用注解事务并且中途手动提交", notes = "模拟JOB使用注解事务并且中途手动提交")
    public void processJob2_1() {
        iSpringTransTestService.processJobWtihTransAniAndCommit();
    }

    @PostMapping("processJob3")
    @ApiOperation(value = "模拟JOB使用编程事务", notes = "模拟JOB使用编程事务")
    public void processJob3() {
        iSpringTransTestService.processJobWtihTransCode();
    }

    @PostMapping("selectScheduler")
    @ApiOperation(value = "测试多个调度器", notes = "测试多个调度器")
    public void selectScheduler() {
        Scheduler scheduler = SpringUtil.getBean(QuartzSchedulerFactoryBean2.class).getScheduler();
        log.info(scheduler.toString());
    }
}
