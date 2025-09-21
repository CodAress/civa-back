package com.civa.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CivaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CivaBackendApplication.class, args);
    }

}
