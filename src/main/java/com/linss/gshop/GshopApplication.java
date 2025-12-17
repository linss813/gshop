package com.linss.gshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@EnableJpaAuditing
public class GshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(GshopApplication.class, args);
    }

}
