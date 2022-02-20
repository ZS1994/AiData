package com.zs.aidata.core.tools;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.RemoteScheduler;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.JobFactory;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.quartz.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * 原生的SchedulerFactoryBean只会开启一个调度器，当有需要用到两个调度器时，比如需要两个线程池，单独执行任务，所以我这边写第二个调度器，
 * 可以通过配置文件的配置项来控制是否启动
 *
 * @author 张顺
 * @自 2004 年 2 月 18 日起
 * @see #setDataSource
 * @see org.quartz.Scheduler
 * @see org.quartz.SchedulerFactory
 * @see org.quartz.impl.StdSchedulerFactory
 * @see org.springframework.transaction.interceptor.TransactionProxyFactoryBean
 * @since 2021-12-26
 * {@link FactoryBean} 创建和配置 Quartz {@link org.quartz.Scheduler}，
 * 作为 Spring 应用程序上下文的一部分管理其生命周期，并公开
 * 调度程序作为依赖注入的 bean 参考。
 *
 * <p>允许自动注册 JobDetails、日历和触发器
 * 在初始化时启动调度程序并在销毁时关闭它。
 * 在只需要在启动时静态注册作业的场景中，有
 * 不需要在应用程序代码中访问调度程序实例本身。
 *
 * <p>为了在运行时动态注册作业，使用 bean 引用
 * 这个 SchedulerFactoryBean 可以直接访问 Quartz Scheduler
 * ({@code org.quartz.Scheduler})。这允许您创建新工作
 * 和触发器，还可以控制和监视整个调度程序。
 *
 * <p>注意 Quartz 每次执行都会实例化一个新的 Job，在
 * 与使用共享的 TimerTask 实例的 Timer 形成对比
 * 在重复执行之间。仅共享 JobDetail 描述符。
 *
 * <p>当使用持久作业时，强烈建议执行所有
 * 在 Spring 管理（或普通 JTA）事务中的调度器上的操作。
 * 否则，数据库锁定将无法正常工作，甚至可能会中断。
 * （有关详细信息，请参阅 {@link #setDataSource setDataSource} javadoc。）
 *
 * <p>实现事务执行的首选方式是划界
 * 业务门面级别的声明性事务，这将
 * 自动应用于在这些范围内执行的调度程序操作。
 * 或者，您可以为调度程序本身添加事务建议。
 *
 * <p>从 Spring 4.1 开始，与 Quartz 2.1.4 及更高版本兼容。
 */

/**
 * 暂时废弃，经过测试这种方法不行，因为整个quartz就一个线程池
 */
//@Component
@Deprecated
public class QuartzSchedulerFactoryBean2 extends SchedulerAccessor implements FactoryBean<Scheduler>,
        BeanNameAware, ApplicationContextAware, InitializingBean, DisposableBean, SmartLifecycle {

    /**
     * The thread count property.
     */
    public static final String PROP_THREAD_COUNT = "org.quartz.threadPool.threadCount";

    /**
     * The default thread count.
     */
    public static final int DEFAULT_THREAD_COUNT = 10;


    private static final ThreadLocal<ResourceLoader> configTimeResourceLoaderHolder = new ThreadLocal<>();

    private static final ThreadLocal<Executor> configTimeTaskExecutorHolder = new ThreadLocal<>();

    private static final ThreadLocal<DataSource> configTimeDataSourceHolder = new ThreadLocal<>();

    private static final ThreadLocal<DataSource> configTimeNonTransactionalDataSourceHolder = new ThreadLocal<>();


    /**
     * 返回当前配置的 Quartz Scheduler 的 {@link ResourceLoader}，
     * 由 {@link ResourceLoaderClassLoadHelper} 使用。
     * <p>这个实例会在对应的Scheduler初始化之前设置，
     * 并在之后立即重置。 因此，它仅在配置期间可用。
     *
     * @see #setApplicationContext
     * @see ResourceLoaderClassLoadHelper
     */
    @Nullable
    public static ResourceLoader getConfigTimeResourceLoader() {
        return configTimeResourceLoaderHolder.get();
    }

    /**
     * 为当前配置的 Quartz Scheduler 返回 {@link Executor}，
     * 由 {@link LocalTaskExecutorThreadPool} 使用。
     * <p>这个实例会在对应的Scheduler初始化之前设置，
     * 并在之后立即重置。 因此，它仅在配置期间可用。
     *
     * @自 2.0
     * @see #setTaskExecutor
     * @see LocalTaskExecutorThreadPool
     */
    @Nullable
    public static Executor getConfigTimeTaskExecutor() {
        return configTimeTaskExecutorHolder.get();
    }

    /**
     * 返回当前配置的 Quartz Scheduler 的 {@link DataSource}，
     * 由 {@link LocalDataSourceJobStore} 使用。
     * <p>这个实例会在对应的Scheduler初始化之前设置，
     * 并在之后立即重置。 因此，它仅在配置期间可用。
     *
     * @自 1.1
     * @see #setDataSource
     * @see LocalDataSourceJobStore
     */
    @Nullable
    public static DataSource getConfigTimeDataSource() {
        return configTimeDataSourceHolder.get();
    }

    /**
     * 返回当前配置的非事务性 {@link DataSource}
     * Quartz 调度器，由 {@link LocalDataSourceJobStore} 使用。
     * <p>这个实例会在对应的Scheduler初始化之前设置，
     * 并在之后立即重置。 因此，它仅在配置期间可用。
     *
     * @自 1.1
     * @see #setNonTransactionalDataSource
     * @see LocalDataSourceJobStore
     */
    @Nullable
    public static DataSource getConfigTimeNonTransactionalDataSource() {
        return configTimeNonTransactionalDataSourceHolder.get();
    }


    @Nullable
    private SchedulerFactory schedulerFactory;

    private Class<? extends SchedulerFactory> schedulerFactoryClass = StdSchedulerFactory.class;

    /**
     * 张顺，2021-12-26 给一个默认值，区分开默认的调度器
     */
    @Nullable
    private String schedulerName = "scheduler2";

    @Nullable
    private Resource configLocation;

    @Nullable
    private Properties quartzProperties;

    @Nullable
    private Executor taskExecutor;

    @Nullable
    private DataSource dataSource;

    @Nullable
    private DataSource nonTransactionalDataSource;

    @Nullable
    private Map<String, ?> schedulerContextMap;

    @Nullable
    private String applicationContextSchedulerContextKey;

    @Nullable
    private JobFactory jobFactory;

    private boolean jobFactorySet = false;

    private boolean autoStartup = true;

    private int startupDelay = 0;

    private int phase = DEFAULT_PHASE;

    private boolean exposeSchedulerInRepository = false;

    private boolean waitForJobsToCompleteOnShutdown = false;

    @Nullable
    private String beanName;

    @Nullable
    private ApplicationContext applicationContext;

    @Nullable
    private Scheduler scheduler;


    /**
     * 设置要使用的外部 Quartz {@link SchedulerFactory} 实例。
     * <p>默认是一个内部 {@link StdSchedulerFactory} 实例。 如果这个方法是
     * 调用，它会覆盖通过 {@link #setSchedulerFactoryClass} 指定的任何类
     * 以及通过 {@link #setConfigLocation} 指定的任何设置，
     * {@link #setQuartzProperties}、{@link #setTaskExecutor} 或 {@link #setDataSource}。
     * <p><b>注意：</b> 使用外部提供的 {@code SchedulerFactory} 实例，
     * 本地设置，例如 {@link #setConfigLocation} 或 {@link #setQuartzProperties}
     * 将在 {@code SchedulerFactoryBean} 中被忽略，期待外部
     * {@code SchedulerFactory} 实例自行初始化。
     *
     * @自 4.3.15 起
     * @see #setSchedulerFactoryClass
     */
    public void setSchedulerFactory(SchedulerFactory schedulerFactory) {
        this.schedulerFactory = schedulerFactory;
    }

    /**
     * 设置要使用的 Quartz {@link SchedulerFactory} 实现。
     * <p>默认为 {@link StdSchedulerFactory} 类，读入标准
     * {@codequartz.properties} 来自 {@codequartz.jar}。 用于应用自定义石英
     * 属性，指定 {@link #setConfigLocation "configLocation"} 和/或
     * {@link #setQuartzProperties "quartzProperties"} 等在这个本地
     * {@code SchedulerFactoryBean} 实例。
     *
     * @see org.quartz.impl.StdSchedulerFactory
     * @see #setConfigLocation
     * @see #setQuartzProperties
     * @see #setTaskExecutor
     * @see #setDataSource
     */
    public void setSchedulerFactoryClass(Class<? extends SchedulerFactory> schedulerFactoryClass) {
        this.schedulerFactoryClass = schedulerFactoryClass;
    }

    /**
     * 通过SchedulerFactory 设置要创建的Scheduler 的名称，作为
     * 替代 {@code org.quartz.scheduler.instanceName} 属性。
     * <p>如果未指定，名称将取自 Quartz 属性
     * ({@code org.quartz.scheduler.instanceName})，或来自声明的
     * {@code SchedulerFactoryBean} bean 名称作为后备。
     *
     * @see #setBeanName
     * @see StdSchedulerFactory#PROP_SCHED_INSTANCE_NAME
     * @see org.quartz.SchedulerFactory#getScheduler()
     * @see org.quartz.SchedulerFactory#getScheduler(String)
     */
    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    /**
     * 设置 Quartz 属性配置文件的位置，例如
     * 作为类路径资源“classpath:quartz.properties”。
     * <p>注意：当指定了所有必要的属性时可以省略
     * 通过这个 bean 在本地，或者在依赖 Quartz 的默认配置时。
     *
     * @see #setQuartzProperties
     */
    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    /**
     * 设置 Quartz 属性，如“org.quartz.threadPool.class”。
     * <p>可用于覆盖 Quartz 属性配置文件中的值，
     * 或在本地指定所有必要的属性。
     *
     * @see #setConfigLocation
     */
    public void setQuartzProperties(Properties quartzProperties) {
        this.quartzProperties = quartzProperties;
    }

    /**
     * 设置一个 Spring 管理的 {@link Executor} 作为 Quartz 后端。
     * 通过 Quartz SPI 作为线程池公开。
     * <p>可用于分配本地JDK ThreadPoolExecutor 或CommonJ
     * WorkManager 作为 Quartz 后端，避免 Quartz 手动创建线程。
     * <p>默认情况下，会使用一个 Quartz SimpleThreadPool，通过配置
     * 对应的 Quartz 属性。
     *
     * @自 2.0
     * @see #setQuartzProperties
     * @see LocalTaskExecutorThreadPool
     * @see org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
     * @see org.springframework.scheduling.concurrent.DefaultManagedTaskExecutor
     */
    public void setTaskExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * 设置调度程序使用的默认 {@link DataSource}。
     * 如果设置，这将覆盖 Quartz 属性中的相应设置。
     * <p>注意：如果设置了这个，Quartz 设置不应该定义
     * 一个作业存储“dataSource”，以避免无意义的双重配置。
     * <p>将使用 Quartz 的 JobStoreCMT 特定于 Spring 的子类。
     * 因此强烈建议在
     * Spring 管理（或普通 JTA）事务中的调度程序。
     * 否则，数据库锁定将无法正常工作，甚至可能会中断
     * （例如，如果尝试在没有事务的情况下获取 Oracle 上的锁）。
     * <p>支持事务性和非事务性数据源访问。
     * 使用非 XA 数据源和本地 Spring 事务，单个数据源
     * 参数就足够了。在 XA 数据源和全局 JTA 事务的情况下，
     * 应该设置 SchedulerFactoryBean 的“nonTransactionalDataSource”属性，
     * 传入一个不会参与全局事务的非 XA 数据源。
     *
     * @自 1.1
     * @see #setNonTransactionalDataSource
     * @see #setQuartzProperties
     * @see #setTransactionManager
     * @see LocalDataSourceJobStore
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 设置 {@link DataSource} 用于<i>非事务性访问</i>。
     * <p>仅当默认数据源是 XA 数据源时才需要
     * 始终参与交易：该数据源的非 XA 版本应该
     * 在这种情况下被指定为“nonTransactionalDataSource”。
     * <p>这与本地 DataSource 实例和 Spring 事务无关。
     * 将单个默认数据源指定为“数据源”就足够了。
     *
     * @自 1.1
     * @see #setDataSource
     * @see LocalDataSourceJobStore
     */
    public void setNonTransactionalDataSource(DataSource nonTransactionalDataSource) {
        this.nonTransactionalDataSource = nonTransactionalDataSource;
    }

    /**
     * 通过给定的 Map 在调度程序上下文中注册对象。
     * 这些对象将可用于在此调度程序中运行的任何作业。
     * <p>注意：当使用持久性作业时，其 JobDetail 将保存在
     * 数据库，不要放 Spring 管理的 bean 或 ApplicationContext
     * 引用到 JobDataMap 而是引用到 SchedulerContext。
     *
     * @param schedulerContextAsMap 带有字符串键和任何对象的映射
     *                              值（例如 Spring 管理的 bean）
     * @see JobDetailFactoryBean#setJobDataAsMap
     */
    public void setSchedulerContextAsMap(Map<String, ?> schedulerContextAsMap) {
        this.schedulerContextMap = schedulerContextAsMap;
    }

    /**
     * 设置 {@link ApplicationContext} 引用的键以在
     * SchedulerContext，例如“applicationContext”。默认为无。
     * 仅适用于在 Spring ApplicationContext 中运行时。
     * <p>注意：当使用持久性作业时，其 JobDetail 将保存在
     * 数据库，不要将 ApplicationContext 引用放入 JobDataMap
     * 而是进入 SchedulerContext。
     * <p>在 QuartzJobBean 的情况下，引用将应用于作业
     * 实例作为 bean 属性。 “applicationContext”属性将
     * 对应于该场景中的“setApplicationContext”方法。
     * <p>注意 BeanFactory 回调接口，如 ApplicationContextAware
     * 不会自动应用于 Quartz Job 实例，因为 Quartz
     * 本身负责其 Jobs 的生命周期。
     *
     * @see JobDetailFactoryBean#setApplicationContextJobDataKey
     * @see org.springframework.context.ApplicationContext
     */
    public void setApplicationContextSchedulerContextKey(String applicationContextSchedulerContextKey) {
        this.applicationContextSchedulerContextKey = applicationContextSchedulerContextKey;
    }

    /**
     * 设置 Quartz {@link JobFactory} 用于此调度程序。
     * <p>默认是Spring的{@link AdaptableJobFactory}，它支持
     * {@link java.lang.Runnable} 对象以及标准 Quartz
     * {@link org.quartz.Job} 实例。 请注意，此默认值仅适用
     * 到 <i>local</i> 调度程序，而不是远程调度程序（其中设置
     * Quartz 不支持自定义 JobFactory）。
     * <p>在此处指定 Spring 的 {@link SpringBeanJobFactory} 实例
     * （通常作为内部 bean 定义）自动填充作业的
     * 来自指定作业数据映射和调度程序上下文的 bean 属性。
     *
     * @自 2.0
     * @see AdaptableJobFactory
     * @see SpringBeanJobFactory
     */
    public void setJobFactory(JobFactory jobFactory) {
        this.jobFactory = jobFactory;
        this.jobFactorySet = true;
    }

    /**
     * 设置初始化后是否自动启动调度器。
     * <p>默认为“真”； 将此设置为“false”以允许手动启动。
     */
    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    /**
     * 返回此调度程序是否配置为自动启动。 如果是真的”，
     * 调度程序将在上下文刷新后启动
     * 启动延迟，如果有的话。
     */
    @Override
    public boolean isAutoStartup() {
        return this.autoStartup;
    }

    /**
     * 指定应该启动和停止此调度程序的阶段。
     * 启动顺序从低到高，关机顺序
     * 是相反的。 默认情况下，此值为 {@code Integer.MAX_VALUE}
     * 意味着这个调度器尽可能晚地开始并尽快停止
     * 尽可能。
     *
     * @自 3.0
     */
    public void setPhase(int phase) {
        this.phase = phase;
    }

    /**
     * 返回此调度程序将启动和停止的阶段。
     */
    @Override
    public int getPhase() {
        return this.phase;
    }

    /**
     * 设置之前初始化后等待的秒数
     * 异步启动调度程序。 默认为 0，意思是
     * 初始化此 bean 时立即同步启动。
     * <p>如果没有作业，将其设置为 10 或 20 秒是有意义的
     * 应该在整个应用程序启动之前运行。
     */
    public void setStartupDelay(int startupDelay) {
        this.startupDelay = startupDelay;
    }

    /**
     * 设置是否暴露Spring管理的{@link Scheduler}实例在
     * Quartz {@link SchedulerRepository}。 默认为“false”，因为 Spring 管理
     * 调度程序通常专门用于在 Spring 上下文中进行访问。
     * <p>将此标志切换为“true”，以便在全局范围内公开调度程序。
     * 除非您有一个现有的 Spring 应用程序，否则不建议这样做
     * 依赖于这种行为。 请注意，这种全球暴露是偶然的
     * 早期 Spring 版本中的默认值； 从 Spring 2.5.6 开始，这已得到修复。
     */
    public void setExposeSchedulerInRepository(boolean exposeSchedulerInRepository) {
        this.exposeSchedulerInRepository = exposeSchedulerInRepository;
    }

    /**
     * 设置是否在关机时等待正在运行的作业完成。
     * <p>默认为“假”。 如果您愿意，请将其切换为“true”
     * 以更长的关闭阶段为代价完全完成工作。
     *
     * @see org.quartz.Scheduler#shutdown(boolean)
     */
    public void setWaitForJobsToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
        this.waitForJobsToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    //---------------------------------------------------------------------
    // InitializingBean 接口的实现
    //---------------------------------------------------------------------
    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.dataSource == null && this.nonTransactionalDataSource != null) {
            this.dataSource = this.nonTransactionalDataSource;
        }

        if (this.applicationContext != null && this.resourceLoader == null) {
            this.resourceLoader = this.applicationContext;
        }

        // Initialize the Scheduler instance...
        this.scheduler = prepareScheduler(prepareSchedulerFactory());
        try {
            registerListeners();
            registerJobsAndTriggers();
        } catch (Exception ex) {
            try {
                this.scheduler.shutdown(true);
            } catch (Exception ex2) {
                logger.debug("注册失败后调度程序关闭异常", ex2);
            }
            throw ex;
        }
    }


    /**
     * 如有必要，创建一个 SchedulerFactory 并将本地定义的 Quartz 属性应用到它。
     *
     * @return 初始化的SchedulerFactory
     */
    private SchedulerFactory prepareSchedulerFactory() throws SchedulerException, IOException {
        SchedulerFactory schedulerFactory = this.schedulerFactory;
        if (schedulerFactory == null) {
            // 创建本地 SchedulerFactory 实例（通常是 StdSchedulerFactory）
            schedulerFactory = BeanUtils.instantiateClass(this.schedulerFactoryClass);
            if (schedulerFactory instanceof StdSchedulerFactory) {
                initSchedulerFactory((StdSchedulerFactory) schedulerFactory);
            } else if (this.configLocation != null || this.quartzProperties != null ||
                    this.taskExecutor != null || this.dataSource != null) {
                throw new IllegalArgumentException(
                        "应用 Quartz 属性所需的 StdSchedulerFactory： " + schedulerFactory);
            }
            // 否则，不会通过 StdSchedulerFactory.initialize(Properties) 应用本地设置
        }
        // 否则，假设外部提供的工厂已经用适当的设置初始化
        return schedulerFactory;
    }

    /**
     * 初始化给定的 SchedulerFactory，将本地定义的 Quartz 属性应用到它。
     *
     * @param schedulerFactory 要初始化的 SchedulerFactory
     */
    private void initSchedulerFactory(StdSchedulerFactory schedulerFactory) throws SchedulerException, IOException {
        Properties mergedProps = new Properties();
        if (this.resourceLoader != null) {
            mergedProps.setProperty(StdSchedulerFactory.PROP_SCHED_CLASS_LOAD_HELPER_CLASS,
                    ResourceLoaderClassLoadHelper.class.getName());
        }

        if (this.taskExecutor != null) {
            mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS,
                    LocalTaskExecutorThreadPool.class.getName());
        } else {
            // 在这里设置必要的默认属性，因为 Quartz 不适用
            // 明确给出属性时的默认配置。
            mergedProps.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, SimpleThreadPool.class.getName());
            mergedProps.setProperty(PROP_THREAD_COUNT, Integer.toString(DEFAULT_THREAD_COUNT));
        }

        if (this.configLocation != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Loading Quartz config from [" + this.configLocation + "]");
            }
            PropertiesLoaderUtils.fillProperties(mergedProps, this.configLocation);
        }

        CollectionUtils.mergePropertiesIntoMap(this.quartzProperties, mergedProps);
        if (this.dataSource != null) {
            mergedProps.setProperty(StdSchedulerFactory.PROP_JOB_STORE_CLASS, LocalDataSourceJobStore.class.getName());
        }

        // 通过本地设置和 Quartz 属性确定调度程序名称...
        if (this.schedulerName != null) {
            mergedProps.setProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, this.schedulerName);
        } else {
            String nameProp = mergedProps.getProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME);
            if (nameProp != null) {
                this.schedulerName = nameProp;
            } else if (this.beanName != null) {
                mergedProps.setProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME, this.beanName);
                this.schedulerName = this.beanName;
            }
        }

        schedulerFactory.initialize(mergedProps);
    }

    /**
     * 准备调度器
     *
     * @param schedulerFactory 调度器工厂
     * @return 调度器
     * @throws SchedulerException 异常
     */
    private Scheduler prepareScheduler(SchedulerFactory schedulerFactory) throws SchedulerException {
        if (this.resourceLoader != null) {
            // 使给定的 ResourceLoader 可用于 SchedulerFactory 配置。
            configTimeResourceLoaderHolder.set(this.resourceLoader);
        }
        if (this.taskExecutor != null) {
            // 使给定的 TaskExecutor 可用于 SchedulerFactory 配置。
            configTimeTaskExecutorHolder.set(this.taskExecutor);
        }
        if (this.dataSource != null) {
            // 使给定的数据源可用于 SchedulerFactory 配置。
            configTimeDataSourceHolder.set(this.dataSource);
        }
        if (this.nonTransactionalDataSource != null) {
            // 使给定的非事务性数据源可用于 SchedulerFactory 配置。
            configTimeNonTransactionalDataSourceHolder.set(this.nonTransactionalDataSource);
        }

        // 从 SchedulerFactory 中获取 Scheduler 实例。
        try {
            Scheduler scheduler = createScheduler(schedulerFactory, this.schedulerName);
            populateSchedulerContext(scheduler);

            if (!this.jobFactorySet && !(scheduler instanceof RemoteScheduler)) {
                // 使用 AdaptableJobFactory 作为本地调度程序的默认值，除非
                // 通过“jobFactory”bean 属性明确给出一个空值。
                this.jobFactory = new AdaptableJobFactory();
            }
            if (this.jobFactory != null) {
                if (this.applicationContext != null && this.jobFactory instanceof ApplicationContextAware) {
                    ((ApplicationContextAware) this.jobFactory).setApplicationContext(this.applicationContext);
                }
                if (this.jobFactory instanceof SchedulerContextAware) {
                    ((SchedulerContextAware) this.jobFactory).setSchedulerContext(scheduler.getContext());
                }
                scheduler.setJobFactory(this.jobFactory);
            }
            return scheduler;
        } finally {
            if (this.resourceLoader != null) {
                configTimeResourceLoaderHolder.remove();
            }
            if (this.taskExecutor != null) {
                configTimeTaskExecutorHolder.remove();
            }
            if (this.dataSource != null) {
                configTimeDataSourceHolder.remove();
            }
            if (this.nonTransactionalDataSource != null) {
                configTimeNonTransactionalDataSourceHolder.remove();
            }
        }
    }

    /**
     * 为给定的工厂和调度程序名称创建调度程序实例。
     * 由 {@link #afterPropertiesSet} 调用。
     * <p>默认实现调用SchedulerFactory的{@code getScheduler}
     * 方法。 可以覆盖自定义调度程序创建。
     *
     * @param schedulerFactory 用于创建调度程序的工厂
     * @param schedulerName    要创建的调度程序的名称
     * @return 调度器实例
     * @throws SchedulerException 如果由 Quartz 方法抛出
     * @see #afterPropertiesSet
     * @see org.quartz.SchedulerFactory#getScheduler
     */
    protected Scheduler createScheduler(SchedulerFactory schedulerFactory, @Nullable String schedulerName)
            throws SchedulerException {

        // 覆盖线程上下文 ClassLoader 以解决简单的 Quartz ClassLoadHelper 加载。
        Thread currentThread = Thread.currentThread();
        ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
        boolean overrideClassLoader = (this.resourceLoader != null &&
                this.resourceLoader.getClassLoader() != threadContextClassLoader);
        if (overrideClassLoader) {
            currentThread.setContextClassLoader(this.resourceLoader.getClassLoader());
        }
        try {
            SchedulerRepository repository = SchedulerRepository.getInstance();
            synchronized (repository) {
                Scheduler existingScheduler = (schedulerName != null ? repository.lookup(schedulerName) : null);
                Scheduler newScheduler = schedulerFactory.getScheduler();
                if (newScheduler == existingScheduler) {
                    throw new IllegalStateException("名称为'" + schedulerName + "'的活动调度程序已经" +
                            "已注册" +
                            "在 Quartz SchedulerRepository 中。无法创建相同的 Spring 管理的新调度程序" +
                            "名称！");
                }
                if (!this.exposeSchedulerInRepository) {
                    // 在这种情况下需要删除它，因为 Quartz 默认共享 Scheduler 实例！
                    SchedulerRepository.getInstance().remove(newScheduler.getSchedulerName());
                }
                return newScheduler;
            }
        } finally {
            if (overrideClassLoader) {
                // 重置原始线程上下文 ClassLoader。
                currentThread.setContextClassLoader(threadContextClassLoader);
            }
        }
    }

    /**
     * 公开指定的上下文属性和/或当前
     * Quartz SchedulerContext 中的ApplicationContext。
     */
    private void populateSchedulerContext(Scheduler scheduler) throws SchedulerException {
        // 将指定的对象放入调度程序上下文中。
        if (this.schedulerContextMap != null) {
            scheduler.getContext().putAll(this.schedulerContextMap);
        }

        // 在调度程序上下文中注册 ApplicationContext。
        if (this.applicationContextSchedulerContextKey != null) {
            if (this.applicationContext == null) {
                throw new IllegalStateException(
                        "SchedulerFactoryBean 需要在 ApplicationContext 中设置" +
                                "能够处理'applicationContextSchedulerContextKey'");
            }
            scheduler.getContext().put(this.applicationContextSchedulerContextKey, this.applicationContext);
        }
    }


    /**
     * 启动 Quartz Scheduler，尊重“startupDelay”设置。
     *
     * @param scheduler    要启动的调度程序
     * @param startupDelay 启动前等待的秒数
     *                     异步调度器
     */
    protected void startScheduler(final Scheduler scheduler, final int startupDelay) throws SchedulerException {
        if (startupDelay <= 0) {
            logger.info("现在启动 Quartz 调度器");
            scheduler.start();
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("将启动 Quartz Scheduler [" + scheduler.getSchedulerName() +
                        "] in " + startupDelay + " seconds");
            }
            // 不使用 Quartz startDelayed 方法，因为我们明确想要一个守护进程
            // 这里的线程，在所有其他线程结束的情况下不保持 JVM 处于活动状态。
            Thread schedulerThread = new Thread() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(startupDelay);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        // 简单地继续
                    }
                    if (logger.isInfoEnabled()) {
                        logger.info("现在启动 Quartz Scheduler，延迟 " + startupDelay + " 秒后");
                    }
                    try {
                        scheduler.start();
                    } catch (SchedulerException ex) {
                        throw new SchedulingException("延迟后无法启动 Quartz Scheduler", ex);
                    }
                }
            };
            schedulerThread.setName("Quartz Scheduler [" + scheduler.getSchedulerName() + "]");
            schedulerThread.setDaemon(true);
            schedulerThread.start();
        }
    }


    //---------------------------------------------------------------------
    // FactoryBean接口的实现
    //---------------------------------------------------------------------
    @Override
    public Scheduler getScheduler() {
        Assert.state(this.scheduler != null, "未设置调度程序");
        return this.scheduler;
    }

    @Override
    @Nullable
    public Scheduler getObject() {
        return this.scheduler;
    }

    @Override
    public Class<? extends Scheduler> getObjectType() {
        return (this.scheduler != null ? this.scheduler.getClass() : Scheduler.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    //---------------------------------------------------------------------
    // SmartLifecycle接口的实现
    //---------------------------------------------------------------------
    @Override
    public void start() throws SchedulingException {
        if (this.scheduler != null) {
            try {
                startScheduler(this.scheduler, this.startupDelay);
            } catch (SchedulerException ex) {
                throw new SchedulingException("无法启动 Quartz 调度程序", ex);
            }
        }
    }

    @Override
    public void stop() throws SchedulingException {
        if (this.scheduler != null) {
            try {
                this.scheduler.standby();
            } catch (SchedulerException ex) {
                throw new SchedulingException("无法启动 Quartz 调度程序", ex);
            }
        }
    }

    @Override
    public boolean isRunning() throws SchedulingException {
        if (this.scheduler != null) {
            try {
                return !this.scheduler.isInStandbyMode();
            } catch (SchedulerException ex) {
                return false;
            }
        }
        return false;
    }


    //---------------------------------------------------------------------
    // Implementation of DisposableBean interface
    //---------------------------------------------------------------------

    /**
     * 在 bean factory 关闭时关闭 Quartz 调度程序，
     * 停止所有预定的作业。
     */
    @Override
    public void destroy() throws SchedulerException {
        if (this.scheduler != null) {
            logger.info("关闭 Quartz 调度器");
            this.scheduler.shutdown(this.waitForJobsToCompleteOnShutdown);
        }
    }

}
