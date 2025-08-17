package com.integritylabsbrazil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class ApiWebGuidApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ApiWebGuidApplication.class, args);
    }

}
