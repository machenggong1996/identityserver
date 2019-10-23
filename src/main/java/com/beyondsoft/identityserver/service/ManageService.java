package com.beyondsoft.identityserver.service;

import com.beyondsoft.identityserver.pojo.UpdatePasswordDO;
import com.beyondsoft.identityserver.pojo.UserModel;
import com.beyondsoft.identityserver.pojo.UserPasswordUpdateModel;
import com.beyondsoft.identityserver.util.ResponseResult;
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
