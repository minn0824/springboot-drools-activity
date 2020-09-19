package com.minn.springbootdroolsactivity;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpringbootDroolsActivityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDroolsActivityApplication.class, args);
    }

}
