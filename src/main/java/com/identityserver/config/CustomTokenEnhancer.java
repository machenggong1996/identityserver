package com.identityserver.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.*;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

    /**
     * 自定义一些token属性
     *
     * @param accessToken    accessToken
     * @param authentication authentication
     * @return OAuth2AccessToken
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInformation = new HashMap<>(16);
        // Important !,client_credentials mode ,no user!
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "keypass".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwt");
        Map<String, Object> claims = new HashMap<>();
        if (authentication.getUserAuthentication() != null) {
            // 与登录时候放进去的UserDetail实现类一致
            User user = (User) authentication.getUserAuthentication().getPrincipal();
            additionalInformation.put("grantType", authentication.getOAuth2Request().getGrantType());
            additionalInformation.put("status", 1);
            //授权码模式返回id_token
            if(authentication.getOAuth2Request().getGrantType().equals("authorization_code")){
                List<String> auths = new LinkedList<>();
                for (GrantedAuthority gra : authentication.getAuthorities()) {
                    auths.add(gra.getAuthority().toString());
                }
                claims.put("authorities", auths);
                claims.put("sub", user.getUsername());
                claims.put("scope", accessToken.getScope());
                Long time = new Date().getTime() + 24 * 3600 * 1000;
                String compactJws = Jwts.builder().setHeaderParam("typ", "JWT")
                        .setHeaderParam("alg", "RS256")
                        .setSubject("MyService")
                        .setClaims(claims)//用户身份声明
                        .setIssuedAt(new Date())//认证提供时间
                        .setExpiration(new Date(time))//token过期时间
                        .setIssuer("http://139.219.11.211:30920")//认证提供者
                        .setAudience(authentication.getOAuth2Request().getClientId())//受众
                        .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate()).compact();
                //生成jwt token 考虑加密 编码
                additionalInformation.put("id_token", compactJws);
            }


        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return accessToken;
    }
}
