package com.zs.aidata.core.sys.mytest.service;

/**
 * @author 张顺
 * @since 2021/11/30
 */
public interface ISpringTransTestService {

    void processJob();


    void processJobWtihTransAni();

    void processJobWtihTransAniAndCommit();


    void processJobWtihTransCode();

    /**
     * 模拟下载job
     */
    void processMqs();

    /**
     * 模拟分发job
     */
    void sendMqs();
}
