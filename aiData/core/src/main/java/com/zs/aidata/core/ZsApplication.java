package com.zs.aidata.core;

/**
 * 该项目唯一指定的自定义异常
 *
 * @author 张顺
 * @since 2020/10/18
 */
public class ZsApplication extends RuntimeException {

    /**
     * 会自动替换里面的关键字。{0}，{1}，{2}。。。。{n}。
     * 注意：下标从0开始
     *
     * @param modelMessage
     * @param keyWords
     */
    public ZsApplication(String modelMessage, String... keyWords) {
        for (int i = 0; i < keyWords.length; i++) {
            modelMessage = modelMessage.replace("{" + i + "}", keyWords[i]);
        }
        new ZsApplication(modelMessage);
    }


    public ZsApplication(String message) {
        super(message);
    }
}
