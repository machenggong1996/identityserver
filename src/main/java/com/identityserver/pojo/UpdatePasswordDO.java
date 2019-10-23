package com.identityserver.pojo;

import javax.validation.constraints.NotNull;

/**
 * Created by mcg on 2019/8/19.
 */
public class UpdatePasswordDO {

    @NotNull(message = "userName不能为空")
    private String userName;

    @NotNull(message = "newPassword不能为空")
    private String password;

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
}
