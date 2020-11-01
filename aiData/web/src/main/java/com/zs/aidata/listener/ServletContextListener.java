package com.zs.aidata.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 使用 ApplicationListener 来初始化一些数据到 application 域中的监听器
 *
 * @author 张顺
 * @since 2020/11/1
 */
@Slf4j
public class ServletContextListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 先获取到 application 上下文
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        /*// 获取对应的 service
        UserService userService = applicationContext.getBean(UserService.class);
        User user = userService.getUser();
        // 获取 application 域对象，将查到的信息放到 application 域中
        ServletContext application = applicationContext.getBean(ServletContext.class);
        application.setAttribute("user", user);*/
        log.info("项目启动成功。请访问地址：http://localhost:8080/aidata");
    }
}
