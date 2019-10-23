package com.identityserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserDetailController {
    @GetMapping("/user")
    public Principal user(Principal principal){
        return principal;
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }
}
