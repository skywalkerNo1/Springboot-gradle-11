package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * @author admin
 * 2021/9/320:28
 **/
@Configuration
public class ThreadPoolExecutorConfig {

    @Bean("threadPool")
    public ExecutorService threadPool() {
        //配置核心线程数
        int corePoolSize = 200;
        //配置最大线程数
        int maxPoolSize = 300;
        //空闲线程最大等待时长
        int keepAliveTime = 0;
        ThreadFactory threadFactory = new CustomizableThreadFactory("threadPool-%d");
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}
