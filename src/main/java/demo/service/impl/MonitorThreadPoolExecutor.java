package demo.service.impl;

import demo.service.MonitorTaskService;

import java.util.concurrent.*;

/**
 * @author admin
 * 2021/9/514:57
 **/
public class MonitorThreadPoolExecutor extends ThreadPoolExecutor {

    //用于全局缓存线程
    private static final ConcurrentMap<String, MonitorTaskService> MONITOR_MAP = new ConcurrentHashMap<>(16);

    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public MonitorThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 任务执行前
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        MONITOR_MAP.forEach((id, mts) ->{
            if (mts.usable() && id.equals(((DemoRunner) r).getId())) {
                mts.before(t, r);
            }
        });
    }

    /**
     * 任务执行后
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        MONITOR_MAP.forEach((id, mts) -> {
            if (mts.usable() && id.equals(((DemoRunner) r).getId())) {
                mts.after(r, t);
            }
        });
    }

    /**
     * 添加监控任务
     * @param id 任务唯一id
     * @param task 执行任务
     * @param override 是否覆盖已有监控任务
     */
    public void addMonitorTask(String id, MonitorTaskService task, boolean override) {
        if (override) {
            MONITOR_MAP.put(id, task);
        } else {
            MONITOR_MAP.putIfAbsent(id, task);
        }
    }

    /**
     * 移除监控任务
     * @param id 任务唯一标识
     * @return 返回值
     */
    public MonitorTaskService removeMonitorTask(String id) {
        return MONITOR_MAP.remove(id);
    }

}
