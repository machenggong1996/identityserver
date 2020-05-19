package com.identityserver.controller;

import com.identityserver.util.ResponseResult;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import sun.security.util.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class UserDetailController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeaders httpHeaders;

    @GetMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/hello")
    public String hello() {
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "hello world";
    }

    @GetMapping("/noParamRedirect")
    public RedirectView noParamTest(HttpServletRequest request) {
        RedirectView redirectTarget = new RedirectView();
        redirectTarget.setContextRelative(true);
        redirectTarget.setUrl("oauth/token?password=222" + "&grant_type=password&username=aaa");
        return redirectTarget;
    }

    @GetMapping("/noParamTarget")
    public ResponseResult redirectTarget() {
        ResponseResult result = new ResponseResult();
        //Dept dept = restTemplate.getForObject(DEPT_GET_URL + id, Dept.class);
        JSONObject token = restTemplate
                        .exchange("http://localhost:8080/oauth/token?password=222&grant_type=password&username=aaa",
                                  HttpMethod.GET, new HttpEntity<Object>(this.httpHeaders), JSONObject.class).getBody();
        result.setData(token);
        return result;
    }
}
