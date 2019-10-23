package com.beyondsoft.identityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@ComponentScan("com.beyondsoft.identityserver")
public class IdentityserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityserverApplication.class, args);
    }

}

