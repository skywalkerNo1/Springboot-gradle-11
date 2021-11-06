package demo.service;

import demo.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import demo.service.impl.MonitorThreadPoolExecutor;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyq
 * @since 2021-08-14
 */
public interface UserService extends IService<User> {

    void start(Integer taskId);

    void stop(Integer taskId);

    void threadCount();

    MonitorThreadPoolExecutor getThreadPoolExecutor();
}
