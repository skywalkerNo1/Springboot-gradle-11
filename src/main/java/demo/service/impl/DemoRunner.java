package demo.service.impl;

import demo.utils.ConstantUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author admin
 * 2021/9/320:54
 **/
public class DemoRunner implements Runnable{

    private Integer taskId;
    private Boolean start;
    private RedisTemplate<String, Object> redisTemplate;

    public DemoRunner(Integer taskId, Boolean start, RedisTemplate<String, Object> redisTemplate) {
        this.taskId = taskId;
        this.start = start;
        this.redisTemplate = redisTemplate;
    }

    private void startTask() {
        try {
            String id = String.valueOf(taskId);
            redisTemplate.boundHashOps(ConstantUtil.QC_RUNNER_TASKS_PERFIX).put(id, taskId);
            while (start) {
                if (!redisTemplate.boundHashOps(ConstantUtil.QC_RUNNER_TASKS_PERFIX).hasKey(id)){
                    System.out.println("===========" + Thread.currentThread().getName() +": 任务已停止==========================");
                    break;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println(Thread.currentThread().getName() + ": " + sdf.format(new Date()));
                Thread.sleep(60 * 1000);
                redisTemplate.boundHashOps(ConstantUtil.QC_RUNNER_TASKS_PERFIX).delete(id);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startTask();
    }

    public String getId() {
        return String.valueOf(taskId);
    }
}
