package com.zs.aidata.core.sys.mytest.service;

import com.zs.aidata.core.sys.mytest.dao.ISpringTransTestDao;
import com.zs.aidata.core.sys.mytest.vo.CoreSysTestDO;
import com.zs.aidata.core.tools.BaseCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.inject.Named;
import java.util.List;

/**
 * @author 张顺
 * @since 2021/11/30
 */
@Slf4j
@Named
public class SpringTransTestService extends BaseCoreService implements ISpringTransTestService {


    @Resource
    private ISpringTransTestDao iSpringTransTestDao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 不带事务方法
     */
    @Override
    public void processJob() {
        CoreSysTestDO coreSysTestDO = new CoreSysTestDO();
        coreSysTestDO.setCmf1("1");
        coreSysTestDO.setCmf2("2");
        coreSysTestDO.setCmf3("3");
        iSpringTransTestDao.insertSelective(coreSysTestDO);

        try {
            Thread.sleep(60000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CoreSysTestDO testDO2 = iSpringTransTestDao.selectByPrimaryKey(1);

        testDO2.setCmf1("1111");

        iSpringTransTestDao.updateByPrimaryKey(testDO2);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void processJobWtihTransAni() {
        CoreSysTestDO coreSysTestDO = new CoreSysTestDO();
        coreSysTestDO.setCmf1("1");
        coreSysTestDO.setCmf2("2");
        coreSysTestDO.setCmf3("3");
        iSpringTransTestDao.insertSelective(coreSysTestDO);

        CoreSysTestDO testDO2 = iSpringTransTestDao.selectByPrimaryKey(1);

        testDO2.setCmf1("1111");

        iSpringTransTestDao.updateByPrimaryKey(testDO2);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void processJobWtihTransAniAndCommit() {

        CoreSysTestDO coreSysTestDO = new CoreSysTestDO();
        coreSysTestDO.setCmf1("1");
        coreSysTestDO.setCmf2("2");
        coreSysTestDO.setCmf3("3");
        iSpringTransTestDao.insertSelective(coreSysTestDO);

        CoreSysTestDO testDO2 = iSpringTransTestDao.selectByPrimaryKey(1);

        testDO2.setCmf1("zs");
        iSpringTransTestDao.updateByPrimaryKey(testDO2);

        iSpringTransTestDao.commit();

        testDO2.setCmf1("111122222");
        iSpringTransTestDao.updateByPrimaryKey(testDO2);
        int i = 1 / 0;

    }

    @Override
    public void processJobWtihTransCode() {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setReadOnly(false);
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            CoreSysTestDO coreSysTestDO = new CoreSysTestDO();
            coreSysTestDO.setCmf1("1");
            coreSysTestDO.setCmf2("2");
            coreSysTestDO.setCmf3("3");
            iSpringTransTestDao.insertSelective(coreSysTestDO);

            CoreSysTestDO testDO2 = iSpringTransTestDao.selectByPrimaryKey(1);

            testDO2.setCmf1("1111");

            iSpringTransTestDao.updateByPrimaryKey(testDO2);

            platformTransactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            platformTransactionManager.rollback(status);
        }

        CoreSysTestDO testDO2 = iSpringTransTestDao.selectByPrimaryKey(1);
        testDO2.setCmf1("ZSSSS");
        iSpringTransTestDao.updateByPrimaryKey(testDO2);

        testDO2.setCmf1("ZSSSS222");
        iSpringTransTestDao.updateByPrimaryKey(testDO2);

        int i = 1 / 0;
    }

    @Override
    public void processMqs() {
        log.info("当前线程：{}", Thread.currentThread().getId());

        // 处理前先查询一共有多少个处理中
        CoreSysTestDO query = new CoreSysTestDO();
        // 状态
        query.setCmf1("P");
        // 用户名
        query.setCmf2("test1");
        List<CoreSysTestDO> list = iSpringTransTestDao.selectList(query);
        // 最大并发2
        if (list.size() >= 2) {
            return;
        }

        // 业务逻辑，就用插入一条P来代替
        CoreSysTestDO testDO = new CoreSysTestDO();
        testDO.setCmf1("P");
        testDO.setCmf2("test1");
        iSpringTransTestDao.insert(testDO);
    }

    @Override
    public void sendMqs() {

    }
}
