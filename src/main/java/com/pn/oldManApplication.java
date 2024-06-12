package com.pn;

import cn.undraw.annotation.EnableCool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author pn
 * @date 2024-02-03 08:25
 */
@SpringBootApplication
@MapperScan("com.pn.mapper") // mybatis扫描mapper文件
@EnableScheduling // 开启定时任务
@EnableCool //开启cool-core工具
public class oldManApplication {
    public static void main(String[] args) {
        SpringApplication.run(oldManApplication.class, args);
    }
}
