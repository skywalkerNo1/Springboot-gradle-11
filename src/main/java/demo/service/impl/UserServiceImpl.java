package demo.service.impl;

import demo.model.User;
import demo.dao.UserMapper;
import demo.service.MonitorTaskService;
import demo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import demo.utils.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyq
 * @since 2021-08-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource()
    private RedisTemplate<String, Object> redisTemplate;

    private MonitorThreadPoolExecutor threadPoolExecutor;

    /**
     * 执行任务
     * @param taskId
     */
    @Override
    public void start(Integer taskId) {
        //  获取线程池
        threadPoolExecutor = getThreadPoolExecutor();

        BoundHashOperations bho = redisTemplate.boundHashOps(ConstantUtil.QC_RUNNER_TASKS_PERFIX);
        System.out.println("taskId: " + taskId + "............是否存在, 当前任务数量: " + bho.size());
        String id = String.valueOf(taskId);
        if (!bho.hasKey(id)) {
            threadPoolExecutor.addMonitorTask(id, newTaskService(id), true);
            threadPoolExecutor.execute(new DemoRunner(taskId,true, redisTemplate));
        }
    }

    @Override
    public void stop(Integer taskId) {
        threadPoolExecutor = getThreadPoolExecutor();
        BoundHashOperations bho = redisTemplate.boundHashOps(ConstantUtil.QC_RUNNER_TASKS_PERFIX);
        System.out.println("任务Id: " + taskId + "准备停止， 当前任务数量size: " + bho.size());
        String id = String.valueOf(taskId);
        if (bho.hasKey(id)) {
            bho.delete(id);
            threadPoolExecutor.removeMonitorTask(id);
        }

    }

    @Override
    public void threadCount() {
        threadPoolExecutor = getThreadPoolExecutor();
        System.out.println("当前线程池数量为 size: " + threadPoolExecutor.getActiveCount());

    }


    public MonitorThreadPoolExecutor getThreadPoolExecutor() {
        //配置核心线程数
        int corePoolSize = 200;
        //配置最大线程数
        int maxPoolSize = 300;
        //空闲线程最大等待时长
        int keepAliveTime = 1;
        if (null == threadPoolExecutor) {
            ThreadFactory taskThreadFactory = new CustomizableThreadFactory("task-threadPool-");
            threadPoolExecutor = new MonitorThreadPoolExecutor(corePoolSize,maxPoolSize,
                    keepAliveTime, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1024),
                    taskThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        }
        return threadPoolExecutor;
    }

    private MonitorTaskService newTaskService(String id) {
        return new MonitorTaskService() {
            //  任务记录
            private ThreadLocal<Long> timeRecord = new ThreadLocal<>();
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            public boolean usable() {
                return true;
            }
            //任务执行前
            @Override
            public void before(Thread thread, Runnable runnable) {
                System.out.println("before: " + thread + "->[taskId: " + id + "] ==> 执行于 [ " + LocalDateTime.now().format(dateTimeFormatter) + " ]");
                timeRecord.set(System.currentTimeMillis());
            }
            // 任务执行后
            @Override
            public void after(Runnable runnable, Throwable throwable) {
                long overTime = System.currentTimeMillis();
                Long startTime = timeRecord.get();
                timeRecord.remove();

                if (null == throwable) {
                    System.out.println("after -> [ id: " + id + " ] ==> 正常停止于 [ " + LocalDateTime.now().format(dateTimeFormatter) + ", 任务耗时: " + (overTime - startTime) +" ]");
                } else {
                    System.out.println("after -> [ id: " + id + " ] ==> 异常停止于 [ " + LocalDateTime.now().format(dateTimeFormatter) + ", 任务耗时: " + (overTime - startTime) + ", 异常信息: " + throwable +" ]");
                }
                threadPoolExecutor.removeMonitorTask(id);
            }
        };
    }

}
