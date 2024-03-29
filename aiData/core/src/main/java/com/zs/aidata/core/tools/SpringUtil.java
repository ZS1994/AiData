package com.zs.aidata.core.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 该类已废弃，cn.hutool.extra.spring.SpringUtil是该类的完美上位替代，请用这个
 */
@Deprecated
//@Component
@Slf4j
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
        log.info("---------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------");
        log.info("---------------com.zs.aiData.tools.SpringUtil-------------------------");
        log.info("========ApplicationContext配置成功," +
                "在普通类可以通过调用SpringUtil.getAppContext()获取applicationContext对象," +
                "applicationContext=" + SpringUtil.applicationContext + "========");
        log.info("---------------------------------------------------------------------");
        log.info("---------------------------------------------------------------------");

    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}