package demo.controller;


import demo.model.User;
import demo.service.UserService;
import demo.service.impl.DemoRunner;
import demo.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyq
 * @since 2021-08-14
 */
@RestController
@RequestMapping("/user")
public class UserContrller {

    @Autowired
    private UserService userService;
    @Resource(name = "threadPool")
    private ExecutorService executorService;

    @GetMapping("/start")
    public void start(Integer taskId) {
        userService.start(taskId);
    }

    @GetMapping("/stop")
    public void stop(Integer taskId) {
        userService.stop(taskId);
    }

    @GetMapping("/threadCount")
    public void threadCount() {
        userService.threadCount();
    }

}

