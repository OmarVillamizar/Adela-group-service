package com.example.chaea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.chaea.clients")
public class ChaeaApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ChaeaApplication.class, args);
    }
    
}
