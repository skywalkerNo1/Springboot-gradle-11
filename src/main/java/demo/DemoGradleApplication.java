package demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("demo.dao")
public class DemoGradleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGradleApplication.class, args);
    }

}
