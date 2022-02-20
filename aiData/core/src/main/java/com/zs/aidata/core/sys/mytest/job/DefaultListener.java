package com.zs.aidata.core.sys.mytest.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * 事实证明ftp的这个工具，无法使用job监听器，没有用可能这个框架本身是个bug
 *
 * @author 张顺
 * @since 2021/12/26
 */
@Slf4j
public class DefaultListener implements JobListener {
    SpringTransJobTimeOutThread _timeout;

    public String getName() {
        return "DefaultListener";
    }

    public void jobExecutionVetoed(JobExecutionContext context) {
        // TODO Auto-generated method stub
    }

    public void jobToBeExecuted(JobExecutionContext context) {
        _timeout = new SpringTransJobTimeOutThread(context);
        _timeout.start();
    }

    public void jobWasExecuted(JobExecutionContext context,
                               JobExecutionException jobException) {
        _timeout.interrupt();
    }
}
