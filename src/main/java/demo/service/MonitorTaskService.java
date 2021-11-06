package demo.service;

/**
 * @author admin
 * 2021/9/515:01
 **/
public interface MonitorTaskService {

    /**
     * 当前监控任务是否可用
     * @return true、false
     */
    boolean usable();

    /**
     * 任务执行前
     * @param thread 执行的线程
     * @param runnable 执行的任务
     */
    void before(Thread thread, Runnable runnable);

    /**
     * 任务执行后
     * @param runnable 执行的任务
     * @param throwable 异常信息
     */
    void after(Runnable runnable, Throwable throwable);
}
