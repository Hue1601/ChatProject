package com.example.routervuebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RouterVueBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(RouterVueBeApplication.class, args);
    }
}
