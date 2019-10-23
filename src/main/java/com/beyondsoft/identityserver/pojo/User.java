package com.beyondsoft.identityserver.pojo;

import java.util.List;

/**
 * Created by machenggong on 2019/8/5.
 */
public class User {

    private String userName;

    private String password;

    private List<String> roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
