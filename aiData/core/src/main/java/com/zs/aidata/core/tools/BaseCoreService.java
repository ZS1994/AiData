package com.zs.aidata.core.tools;

import org.springframework.web.client.RestTemplate;

import java.util.Date;

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


    public boolean isEmpty(Object o) {
        return ValueUtils.isEmpty(o);
    }


    public boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }


    public void checkNotEmpty(Object obj, String... fieldNames) throws AiDataApplicationException {
        try {
            ValueUtils.checkNotEmpty(obj, fieldNames);
        } catch (Exception e) {
            throw new AiDataApplicationException(e.toString());
        }
    }

    public void initBaseFieldByCreate(BaseEntityVO entityVO) {
        checkNotEmpty(entityVO);
        entityVO.setCreationById("admin");
        entityVO.setCreationByUser("admin");
        entityVO.setCreationDate(new Date());
        entityVO.setLastUpdatedById("admin");
        entityVO.setLastUpdatedByUser("admin");
        entityVO.setLastUpdatedDate(new Date());
    }


    public void initBaseFieldByUpdate(BaseEntityVO entityVO){
        checkNotEmpty(entityVO);
        entityVO.setLastUpdatedById("admin");
        entityVO.setLastUpdatedByUser("admin");
        entityVO.setLastUpdatedDate(new Date());
    }

}
