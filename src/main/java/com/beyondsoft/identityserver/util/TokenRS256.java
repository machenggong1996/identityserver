package com.beyondsoft.identityserver.util;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import net.minidev.json.JSONObject;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WenChao on 2019/8/14.
 */
public class TokenRS256 {

    /**
     * 这个方法采取的是RS256 非对称加密算法
     */

    public static String TokenTest(String uid,RSAKey rsaJWK) {
        //获取生成token
        Map<String, Object> map = new HashMap<>();
        Date date = new Date();

        //建立载荷，这些数据根据业务，自己定义。
        map.put("uid", uid);
        //生成时间
        map.put("sta", date.getTime());
        //过期时间
        map.put("exp", date.getTime()+6000);
        try {
            String token = TokenUtils.creatTokenRS256(map,rsaJWK);
            return token;
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return null;
    }

    //处理解析的业务逻辑
    public static void ValidToken(String token,RSAKey rsaJWK) {
        //解析token
        try {
            if (token != null) {
                Map<String, Object> validMap = TokenUtils.validRS256(token,rsaJWK);
                int i = (int) validMap.get("Result");
                if (i == 0) {
                    System.out.println("token解析成功");
                    JSONObject jsonObject = (JSONObject) validMap.get("data");
                    System.out.println("uid是：" + jsonObject.get("uid")+" sta是："+jsonObject.get("sta")+" exp是："+jsonObject.get("exp"));
                } else if (i == 2) {
                    System.out.println("token已经过期");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JOSEException {
        //获取token
        String uid = "rs256 test";
        //获取钥匙
        RSAKey key = TokenUtils.getKey();
        //获取token
        String token = TokenTest(uid,key);
        System.out.println("非对称加密的token："+token);
        //解析token
        ValidToken(token,key);
    }

    @Test
    public void jwt() throws Exception {
        JSONObject keyObject = new JSONObject();
        keyObject.put("kty", "RSA");
        keyObject.put("e", "AQAB");
        keyObject.put("n", "p2naRuozp2VPk2-cysifAtwmCiHI2KaSeYwnN_OPr317cEKgfU1zuEtULeQJ_dvzAyC-w7vseUY7OyD2RGVzy8pJPENidSXvRw2Q-EY7Uvz1y0RTkiyhSVkktD66x6eSuuH5gu5ilBMPx6TXwR8jHM3S3h8ilD5YjdXLaQ732g8");
        RSAKey rsaKey = RSAKey.parse(keyObject);
        ValidToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhYWEiLCJhdWQiOiIwMDhmZWMzZC1jMTI1LTQwOWUtOWY4ZC1lZjc3MjRlYzIxZGYiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiaXNzIjoiaHR0cDovLzEzOS4yMTkuMTEuMjExOjMwOTIwIiwiZXhwIjoxNTY1NzUzODcxLCJpYXQiOjE1NjU2Njc0NzEsImF1dGhvcml0aWVzIjpbImFkbWluIiwidXNlciJdfQ.UaOF5E2HttcHckHYEXh7n8GRt897PDbhmJd_Cp3tIelvl7CU4E0lLREbatelSaCGGRebnPL8kZRuYyWYqND9i0dvSvSc083xjqfVWwhL5VOImTJz0d6YUjN1yiYa9v7bq39dsN-y4-yKxhvHbgTEa2b4BsxxQh7-oM54z9UC7Zk",rsaKey);
    }

}
