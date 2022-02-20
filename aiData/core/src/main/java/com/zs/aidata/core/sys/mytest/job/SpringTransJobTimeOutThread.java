package com.zs.aidata.core.sys.mytest.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * job的守护线程，超时检测
 *
 * @author 张顺
 * @since 2021/12/26
 */
@Slf4j
public class SpringTransJobTimeOutThread extends Thread {

    private static long DEFAULT_TIMEOUT = 100l; //缺省的超时时间是100S;
    private JobExecutionContext context;
    private long timeout = -1l; //超时的秒数;

    private SpringTransJobTimeOutThread() {
        super();
    }

    public SpringTransJobTimeOutThread(JobExecutionContext context) {
        this();
        this.context = context;
        if (context != null) {
            Object _tempObj = context.getJobDetail().getJobDataMap().get("TimeOut");
            long _temp = 0l;
            if (_tempObj != null) {
                _temp = (Long) _tempObj;
            }
            this.timeout = _temp > 0l ? _temp : DEFAULT_TIMEOUT;
        }
    }

    public void run() {
        if (timeout == -1)
            return;
        try {
            sleep(timeout * 1000l);
        } catch (InterruptedException ire) {
            log.error(ire.toString());
        }
        try {
            ((QuartzJobBean) context.getJobInstance())
                    .execute(context);
        } catch (JobExecutionException e) {
            throw new RuntimeException("exception", e);
        }
    }

}
