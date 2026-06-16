package com.somosayni.retos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.somosayni.retos", "com.somosayni.shared"})
public class RetosApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetosApplication.class, args);
    }
}
