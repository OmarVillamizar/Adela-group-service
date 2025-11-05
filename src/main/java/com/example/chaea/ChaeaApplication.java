package com.example.chaea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ChaeaApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ChaeaApplication.class, args);
    }
    
}
