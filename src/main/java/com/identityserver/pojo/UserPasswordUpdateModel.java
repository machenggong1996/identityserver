package com.identityserver.pojo;

import javax.validation.constraints.NotNull;

/**
 * Created by machenggong on 2019/8/5.
 */
public class UserPasswordUpdateModel {

    @NotNull(message = "userName不能为空")
    private String userName;
    @NotNull(message = "oldPassword不能为空")
    private String oldPassword;
    @NotNull(message = "newPassword不能为空")
    private String newPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
