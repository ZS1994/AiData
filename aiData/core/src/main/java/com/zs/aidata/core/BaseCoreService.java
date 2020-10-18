package com.zs.aidata.core;

import org.springframework.web.client.RestTemplate;

/**
 * 所有的service都应该继承它
 *
 * @author 张顺
 * @since 2020/10/18
 */
public class BaseCoreService {

    public RestTemplate getRestTemplate() {
        return RestTemplateUtils.getRestemplate();
    }


}
