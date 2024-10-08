package com.supernova.ssagrissakssak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SsagriSsakssakApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsagriSsakssakApplication.class, args);
    }

}
