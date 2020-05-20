package com.identityserver.config;

import com.identityserver.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Order(10)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${security.signing-key}")
    private String signingKey;

    @Value("${security.encoding-strength}")
    private Integer encodingStrength;

    @Value("${security.security-realm}")
    private String securityRealm;

    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
        //        auth.jdbcAuthentication()
        //                .dataSource(dataSource)
        //                //.passwordEncoder(passwordEncoder())
        //                .usersByUsernameQuery(
        //                        "select username,password, enabled from users where username=?")
        //                .authoritiesByUsernameQuery(
        //                        "select username, authority from authorities where username=?");
        //auth.parentAuthenticationManager(authenticationManagerBean());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBeanConfig() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //        http.csrf().disable()
        //                .authorizeRequests()
        //                .antMatchers("/", "/oauth/**", "/login", "/health", "/css/**", "/register/**","/exist/**",
        //                "/updatePasswordByUsername/**","/noParamRedirect/**").permitAll()
        //                .anyRequest().authenticated();
        ////                .and()
        ////                .formLogin()
        ////                .loginPage("/login")
        ////                .permitAll().and().logout();

        http.cors().disable().csrf().disable().authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest)
                        .permitAll().and()
                        //                        .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()//权限禁止
                        //                        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)//认证失败
                        //                        .and()
                        // 基于token，所以不需要session
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                        .authorizeRequests()

                        // 对于获取token的rest api要允许匿名访问
                        .antMatchers("/userlogin", "/api/v1/sign", "/error/**").permitAll()
                        // 除上面外的所有请求全部需要鉴权认证
                        .anyRequest().authenticated();

        // 禁用缓存
        http.headers().cacheControl();

    }
}
