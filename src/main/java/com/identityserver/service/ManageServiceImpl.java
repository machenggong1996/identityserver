package com.identityserver.service;

import com.identityserver.mapper.ManageMapper;
import com.identityserver.pojo.UpdatePasswordDO;
import com.identityserver.pojo.UserModel;
import com.identityserver.pojo.UserPasswordUpdateModel;
import com.identityserver.util.CommonConstants;
import com.identityserver.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.identityserver.util.AuthorityType.ADMIN_ROLE;
import static com.identityserver.util.AuthorityType.USER_ROLE;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private ManageMapper manageMapper;

    @Override
    public void registerUser(UserModel userModel) {
        manageMapper.insertUser(userModel.getUsername(), new BCryptPasswordEncoder().encode(userModel.getPassword()));
        manageMapper.insertAuthority(userModel.getUsername(), ADMIN_ROLE);
    }

    @Override
    public void createUser(UserModel userModel) {
        manageMapper.insertUser(userModel.getUsername(), new BCryptPasswordEncoder().encode(userModel.getPassword()));
        manageMapper.insertAuthority(userModel.getUsername(), USER_ROLE);
    }

    @Override
    public ResponseResult<Boolean> updatePassword(UserPasswordUpdateModel updateModel) {
        String oldPassword = manageMapper.getPasswordByUserName(updateModel.getUserName());
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        if (oldPassword == null) {
            return new ResponseResult<>(false).setCode(CommonConstants.NOT_FOUND).setMsg("用户不存");
        }
        boolean mr = bcryptPasswordEncoder.matches(updateModel.getOldPassword(), oldPassword);
        if (mr) {
            int count = manageMapper.updateUserPassword(updateModel.getUserName(), new BCryptPasswordEncoder().encode(updateModel.getNewPassword()));
            if (count < 1) {
                return new ResponseResult<>(false).setCode(CommonConstants.INNER_ERROR);
            }
            return new ResponseResult<>(true);
        }
        return new ResponseResult<>(false).setCode(CommonConstants.NOT_FOUND).setMsg("密码错误");
    }

    @Override
    public ResponseResult<Boolean> updatePassword1(UpdatePasswordDO passwordDO) {
        String oldPassword = manageMapper.getPasswordByUserName(passwordDO.getUserName());
        if (oldPassword == null) {
            return new ResponseResult<>(false).setCode(CommonConstants.NOT_FOUND).setMsg("用户不存");
        }
        int count = manageMapper.updateUserPassword(passwordDO.getUserName(), new BCryptPasswordEncoder().encode(passwordDO.getPassword()));
        if (count < 1) {
            return new ResponseResult<>(false).setCode(CommonConstants.INNER_ERROR);
        }
        return new ResponseResult<>(true);
    }

    @Override
    public ResponseResult<Boolean> deleteUser(String userName) {
        try {
            manageMapper.deleteAuthoritiesByUserName(userName);
            manageMapper.deleteUserByUserName(userName);
        } catch (Exception e) {
            return new ResponseResult<>(false).setCode(5000).setMsg(e.getMessage());
        }
        return new ResponseResult<>(true);
    }

    /**
     * 判断有用户是否存在
     *
     * @param userName
     * @return
     */
    @Override
    public ResponseResult<Boolean> exist(String userName) {
        Integer count = manageMapper.getByUserName(userName);
        return new ResponseResult<>(count > 0);
    }

}
