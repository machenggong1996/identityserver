package com.identityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.identityserver")
public class IdentityserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityserverApplication.class, args);
    }

}

