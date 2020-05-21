package com.identityserver.util;

import com.identityserver.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author shanshu ()
 * @date 2020/05/19
 */
public class SecurityUtils {

    public static User getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

}
