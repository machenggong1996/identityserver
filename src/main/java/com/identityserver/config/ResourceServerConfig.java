//package com.identityserver.config;
//
//import com.identityserver.exception.CustomWebResponseExceptionTranslator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
///**
// * @author machenggong
// * @date 2020/05/18
// */
//@Configuration
//@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
//public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
//
//    @Value(value = "${spring.security.oauth2.resourceId}")
//    private String oauth2ResourceId;
//
//    @Autowired
//    private CustomAccessDeniedHandler customAccessDeniedHandler;
//
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) {
//        resources.resourceId(oauth2ResourceId);
//        // 定义异常转换类生效
//        AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
//        ((OAuth2AuthenticationEntryPoint) authenticationEntryPoint)
//                        .setExceptionTranslator(new CustomWebResponseExceptionTranslator());
//        resources.authenticationEntryPoint(authenticationEntryPoint);
//    }
//
//    /**
//     * 即作为授权服务又作为资源服务可以不需要check_token
//     */
//
//    //    @Primary
//    //    @Bean
//    //    public RemoteTokenServices tokenServices() {
//    //        final RemoteTokenServices tokenService = new RemoteTokenServices();
//    //        tokenService.setCheckTokenEndpointUrl(String.format("%s/oauth/check_token", host));
//    //        tokenService.setClientId(clientId);
//    //        tokenService.setClientSecret(clientSecret);
//    //        return tokenService;
//    //    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().and().csrf().disable().authorizeRequests().antMatchers("/userlogin","/swagger-ui.html").permitAll()
//                        .antMatchers("/**").access("#oauth2.hasScope('write')").and().exceptionHandling()
//                        .accessDeniedHandler(customAccessDeniedHandler);
//    }
//
//}
