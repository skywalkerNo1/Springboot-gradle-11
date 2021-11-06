package demo.config;

import demo.service.UserService;
import demo.service.impl.DemoRunner;
import demo.service.impl.MonitorThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author admin
 * 2021/9/516:33
 **/
@Configuration
public class MyApplicationRunner implements ApplicationRunner {

    @Resource()
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserService userService;

    private MonitorThreadPoolExecutor threadPoolExecutor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        MonitorThreadPoolExecutor threadPoolExecutor = userService.getThreadPoolExecutor();
//        for (int i = 1; i <= 3; i++) {
//            threadPoolExecutor.execute(new DemoRunner(i, true, redisTemplate));
//        }
    }
}
