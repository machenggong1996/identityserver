package com.identityserver.controller;

import com.identityserver.util.ResponseResult;
import com.identityserver.util.SecurityUtils;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import sun.security.util.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
public class UserDetailController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeaders httpHeaders;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @GetMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/hello")
    public String hello() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        com.identityserver.pojo.User user = SecurityUtils.getUser();
        //Dept dept = restTemplate.getForObject(DEPT_GET_URL + id, Dept.class);
        JSONObject token = restTemplate
                        .exchange("http://localhost:8080/oauth/token?password=222&grant_type=password&username=aaa",
                                  HttpMethod.GET, new HttpEntity<Object>(this.httpHeaders), JSONObject.class).getBody();
        result.setData(token);
        return result;
    }

    @GetMapping("/userlogin")
    public ResponseEntity<OAuth2AccessToken> login(Principal principal) throws HttpRequestMethodNotSupportedException {
        ResponseResult result = new ResponseResult();
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> roles = new LinkedList<>();
        roles.add("user");
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        User user = new User("008fec3d-c125-409e-9f8d-ef7724ec21df", "222", authorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null,
                                                                                                          authorities);
        HashMap<String, String> map = new HashMap<>();
        map.put("password", "222");
        map.put("grant_type", "password");
        map.put("username", "aaa");
        ResponseEntity<OAuth2AccessToken> responseEntity = tokenEndpoint.getAccessToken(authenticationToken, map);
        return responseEntity;
    }
}
