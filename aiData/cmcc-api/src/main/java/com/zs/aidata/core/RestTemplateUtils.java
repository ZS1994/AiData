package com.zs.aidata.core;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @author 张顺
 * @since 2020/10/12
 */
public class RestTemplateUtils {
    private static RestTemplate restTemplate = new RestTemplate();

    public static RestTemplate getRestemplate(){
        //RestTemplate设置编码
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
