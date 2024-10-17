package com.pet_care.manager_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
//@EnableRedisRepositories
public class ManagerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerServiceApplication.class, args);
    }

}
