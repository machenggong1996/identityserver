package com.identityserver.service;

import com.identityserver.pojo.UpdatePasswordDO;
import com.identityserver.pojo.UserModel;
import com.identityserver.pojo.UserPasswordUpdateModel;
import com.identityserver.util.ResponseResult;
import org.springframework.transaction.annotation.Transactional;

public interface ManageService {
    @Transactional
    void registerUser(UserModel userModel);

    @Transactional
    void createUser(UserModel userModel);

    @Transactional
    ResponseResult<Boolean> updatePassword(UserPasswordUpdateModel updateModel);

    @Transactional
    ResponseResult<Boolean> updatePassword1(UpdatePasswordDO passwordDO);

    ResponseResult<Boolean> deleteUser(String userName);

    /**
     * 判断用户是否存在
     *
     * @param userName
     * @return
     */
    ResponseResult<Boolean> exist(String userName);
}
