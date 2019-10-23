package com.identityserver.util;

import com.identityserver.config.TokenBlackListValidator;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.*;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoderJwkSupport;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Created by machenggong on 2019/8/6.
 */
public class RSAUtils {

    private static final String src = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws Exception {
//        System.out.println("\n");
//        RSAKeyPair keyPair = generateKeyPair();
//        System.out.println("公钥：" + keyPair.getPublicKey());
//        System.out.println("私钥：" + keyPair.getPrivateKey());
//        System.out.println("\n");
//        test1(keyPair, src);
//        System.out.println("\n");
//        test2(keyPair, src);
//        System.out.println("\n");

        String pubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnadpG6jOnZU+Tb5zKyJ8C3CYKIcjYppJ5jCc384+vfXtwQqB9TXO4S1Qt5An92/MDIL7Du+x5Rjs7IPZEZXPLykk8Q2J1Je9HDZD4RjtS/PXLRFOSLKFJWSS0PrrHp5K64fmC7mKUEw/HpNfBHyMczdLeHyKUPliN1ctpDvfaDwIDAQAB";
        String text = "eyJzdWIiOiJhYWEiLCJhdWQiOlsic3lzdGVtIl0sInVzZXJfbmFtZSI6ImFhYSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE1NjUwNzU3NDQsImdyYW50VHlwZSI6ImF1dGhvcml6YXRpb25fY29kZSIsImF1dGhvcml0aWVzIjpbImFkbWluIiwidXNlciJdLCJqdGkiOiI2ZGYyN2Q4MC1jMjVlLTQyMGQtODEzMC0wMWU3MmVhN2U5ODkiLCJjbGllbnRfaWQiOiIwMDhmZWMzZC1jMTI1LTQwOWUtOWY4ZC1lZjc3MjRlYzIxZGYiLCJzdGF0dXMiOjF9";
        String text2 = decryptByPublicKey(pubkey, text);
        System.out.println(text2);
    }

    /**
     * 公钥加密私钥解密
     */
    private static void test1(RSAKeyPair keyPair, String source) throws Exception {
        System.out.println("***************** 公钥加密私钥解密开始 *****************");
        String text1 = encryptByPublicKey(keyPair.getPublicKey(), source);
        String text2 = decryptByPrivateKey(keyPair.getPrivateKey(), text1);
        System.out.println("加密前：" + source);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (source.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 公钥加密私钥解密结束 *****************");
    }

    /**
     * 私钥加密公钥解密
     *
     * @throws Exception
     */
    private static void test2(RSAKeyPair keyPair, String source) throws Exception {
        System.out.println("***************** 私钥加密公钥解密开始 *****************");
        String text1 = encryptByPrivateKey(keyPair.getPrivateKey(), source);
        String text2 = decryptByPublicKey(keyPair.getPublicKey(), text1);
        System.out.println("加密前：" + source);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (source.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 私钥加密公钥解密结束 *****************");
    }


    /**
     * 公钥解密
     *
     * @param publicKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyText
     * @param text
     * @return
     */
    public static String encryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static RSAKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        RSAKeyPair rsaKeyPair = new RSAKeyPair(publicKeyString, privateKeyString);
        return rsaKeyPair;
    }


    /**
     * RSA密钥对对象
     */
    public static class RSAKeyPair {

        private String publicKey;
        private String privateKey;

        public RSAKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

    }

    @Test
    public void jwtTest() {
        Key KEY = new SecretKeySpec("123".getBytes(), SignatureAlgorithm.HS256.getJcaName());
//        Map<String, Object> stringObjectMap = new HashMap<>();
//        stringObjectMap.put("type", "1");
//        String payload = "{\"user_id\":\"1341137\", \"expire_time\":\"2018-01-01 0:00:00\"}";
//        String compactJws = Jwts.builder().setHeader(stringObjectMap)
//                .setPayload(payload).signWith(SignatureAlgorithm.HS512, KEY).compact();
//
//        System.out.println("jwt key:" + new String(KEY.getEncoded()));
//        System.out.println("jwt payload:" + payload);
//        System.out.println("jwt encoded:" + compactJws);

        String jwtT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhYWEiLCJhdWQiOlsic3lzdGVtIl0sInVzZXJfbmFtZSI6ImFhYSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE1NjU5NDU5OTUsImdyYW50VHlwZSI6ImF1dGhvcml6YXRpb25fY29kZSIsImF1dGhvcml0aWVzIjpbImFkbWluIiwidXNlciJdLCJqdGkiOiI0Y2NhZDQwZi04NzY1LTQ1OTItOWNmMi0wYTVkMTU1YjllYTkiLCJjbGllbnRfaWQiOiIwMDhmZWMzZC1jMTI1LTQwOWUtOWY4ZC1lZjc3MjRlYzIxZGYiLCJzdGF0dXMiOjF9.WJxNznKVOv3-gq9eI_7U6CEu2pjA9aUyl8D0cRM5UF4";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(KEY).parseClaimsJws(jwtT);
        JwsHeader header = claimsJws.getHeader();
        Claims body = claimsJws.getBody();

        System.out.println("jwt header:" + header);
        System.out.println("jwt body:" + body);
        System.out.println("jwt body user-id:" + body.get("user_id", String.class));


    }

    @Test
    public void jwt() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "keypass".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwt");
        Map<String, Object> claims = new HashMap<>();
        List<String> auths = new LinkedList<>();
        auths.add("admin");
        auths.add("user");
        claims.put("authorities", auths);
        claims.put("sub", "ma");
        Long time = new Date().getTime() + 24 * 3600 * 1000;
        String compactJws = Jwts.builder().setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "RS256")
                .setSubject("ma")
                .setClaims(claims)//用户身份声明
                .setExpiration(new Date(time))//token过期时间
                .setIssuedAt(new Date())//认证提供时间
                .setIssuer("http://139.219.11.211:30920")//认证提供者
                .setAudience("client_id")//受众
                .signWith(SignatureAlgorithm.RS256, keyPair.getPrivate()).compact();
        System.out.println(compactJws);
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        System.out.println("公钥：" + new BASE64Encoder().encodeBuffer(publicKey.getEncoded()));
        System.out.println("私钥：" + new BASE64Encoder().encodeBuffer(privateKey.getEncoded()));
        RSAKey key = new RSAKey.Builder(publicKey).build();
        Map<String, Object> keyMap = new JWKSet(key).toJSONObject();
        NimbusJwtDecoderJwkSupport jwtDecoder = new NimbusJwtDecoderJwkSupport("http://139.219.11.211:30920/.well-known/jwks.json");
        OAuth2TokenValidator<org.springframework.security.oauth2.jwt.Jwt> tokenBlackListValidator = new TokenBlackListValidator();
        OAuth2TokenValidator<org.springframework.security.oauth2.jwt.Jwt> delegatingOAuth2TokenValidator = new DelegatingOAuth2TokenValidator<>(tokenBlackListValidator);
        jwtDecoder.setJwtValidator(delegatingOAuth2TokenValidator);
    }

    private static PrivateKey getPKCS8PrivateKey(String strPk) throws Exception {
        // Remove markers and new line characters in private key
        String realPK = strPk.replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("\n", "");
        byte[] b1 = java.util.Base64.getDecoder().decode(realPK);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    @Test
    public void raskey() throws Exception{
        JSONObject keyObject = new JSONObject();
        keyObject.put("kty","RSA");
        keyObject.put("e","AQAB");
        keyObject.put("n","p2naRuozp2VPk2-cysifAtwmCiHI2KaSeYwnN_OPr317cEKgfU1zuEtULeQJ_dvzAyC-w7vseUY7OyD2RGVzy8pJPENidSXvRw2Q-EY7Uvz1y0RTkiyhSVkktD66x6eSuuH5gu5ilBMPx6TXwR8jHM3S3h8ilD5YjdXLaQ732g8");
        RSAKey rsaKey = RSAKey.parse(keyObject);

    }
}
