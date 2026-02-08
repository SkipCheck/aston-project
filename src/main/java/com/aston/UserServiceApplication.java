package com.aston;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.retry.annotation.EnableRetry;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableRetry
public class UserServiceApplication {

    /**
     * Отображаем сообщение об успешном запуске сервиса
     * после создания контекста Спринга
     */
    @PostConstruct
    public void init(){
        log.info("Сервис контроля пользователей запущен {}", LocalDateTime.now());
    }

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
