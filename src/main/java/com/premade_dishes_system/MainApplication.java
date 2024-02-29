package com.premade_dishes_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.premade_dishes_system.mapper")
@SpringBootApplication(scanBasePackages = {"com.premade_dishes_system"})
public class MainApplication {
    public static void main(String[] args) {
        // 一定要加args参数 不然命令行传入的参数无法生效
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);
    }
}
