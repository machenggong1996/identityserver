package com.identityserver.util;

import com.identityserver.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author shanshu ()
 * @date 2020/05/19
 */
public class SecurityUtils {

    public static User getUser() {
        LinkedHashMap<String, Object> map = (LinkedHashMap) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal();
        LinkedHashMap<String, Object> principalMap = (LinkedHashMap) map.get("principal");
        String userName = (String) principalMap.get("userName");
        List<String> roles = (List<String>) principalMap.get("roles");
        String userId = (String) principalMap.get("userId");
        User user = new User(userName, null, roles, userId);
        return user;
    }

}
