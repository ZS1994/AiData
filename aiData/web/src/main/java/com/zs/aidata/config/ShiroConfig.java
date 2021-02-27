package com.zs.aidata.config;

import com.zs.aidata.realm.MyRealm;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Shiro的配置
 *
 * @author 张顺
 * @since 2021/2/18
 */
@Configuration
public class ShiroConfig {

    /**
     * 配置一个SecurityManager安全管理器
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager(Realm myRealm) {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        webSecurityManager.setRealm(myRealm);
        return webSecurityManager;
    }

    /**
     * 配置一个自定义对的realm的bean，最终将使用这个bean返回哦对象来完成认证和授权
     *
     * @return
     */
    @Bean
    public MyRealm myRealm() {
        MyRealm myRealm = new MyRealm();
        return myRealm;
    }

    /**
     * 配置一个shiro的过滤器，进行一些规则的拦截，例如什么样的请求可以访问什么样的请求不可以访问等等
     *
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /*// 配置用户的登录请求，如果需要进行登录，shiro就要会转到这个请求进入这个登录页面
        shiroFilterFactoryBean.setLoginUrl("/");
        // 配置登录成功后的地址
        shiroFilterFactoryBean.setSuccessUrl("/success");
        // 配置没有权限的地址
        shiroFilterFactoryBean.setUnauthorizedUrl("/noPermission");*/

        // 配置权限的拦截规则
        Map<String, String> filterChainMap = new HashMap<>();
        // 配置登录请求不需要认证
//        filterChainMap.put("/**/login", "anon");
        // 配置登出会清空当前用户的内存
//        filterChainMap.put("/**/logout", "logout");
        // admin开头的请求需要登录认证
//        filterChainMap.put("/admin/**", "authc");
        // 配置剩余请求必须全部需要进行登录认证，注意这个必须写在最后
//        filterChainMap.put("/**", "authc");

        // 设置权限拦截规则
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启shiro的注解支持，例如：@RequiresRoles()   @RequiresUser
     * shiro的注解需要借助spring的aop支持
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 开启shiro的注解支持
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }


    /**
     * 开启shiro的注解的aop支持
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        // 开启AOP的支持
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }

    /**
     * 设置cookie
     *
     * @return
     */
    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        // 设置只有http请求才能使用cookie
//        simpleCookie.setHttpOnly(true);
        // 失效时间，单位是秒  24小时
        simpleCookie.setMaxAge(60 * 60 * 24);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager(SimpleCookie simpleCookie) {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(simpleCookie);
        return cookieRememberMeManager;
    }
}
