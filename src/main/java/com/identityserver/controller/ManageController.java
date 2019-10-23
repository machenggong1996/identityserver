package com.identityserver.controller;

import com.identityserver.pojo.CommonResponse;
import com.identityserver.pojo.UpdatePasswordDO;
import com.identityserver.pojo.UserModel;
import com.identityserver.pojo.UserPasswordUpdateModel;
import com.identityserver.service.ManageService;
import com.identityserver.util.CommonConstants;
import com.identityserver.util.ResponseResult;
import com.identityserver.util.ValidateUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ManageController extends BaseController {
    @Autowired
    private ManageService manageService;

    @Autowired
    private TokenStore tokenStore;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('admin')")
    public CommonResponse createUser(@RequestBody UserModel user) {
        try {
            manageService.createUser(user);
            return success();
        } catch (Exception exception) {
            return failedWithMessage(exception.getMessage());
        }
    }


    @PostMapping("/register")
    public CommonResponse registerUser(@RequestBody UserModel user) {
        try {
            /*PasswordEncoder bpass = new BCryptPasswordEncoder();
            String password = bpass.encode("123456");
            System.out.println(password);*/
            manageService.registerUser(user);
            return success();
        } catch (Exception exception) {
            return failedWithMessage(exception.getMessage());
        }
    }

    @PostMapping("/updatePassword")
    public ResponseResult<Boolean> updatePassword(@RequestBody UserPasswordUpdateModel updateModel, BindingResult bindingResult) {
        ResponseResult validate = ValidateUtils.validate(bindingResult);
        if (validate.getCode() != CommonConstants.SUCCESS) {
            return validate;
        }
        return manageService.updatePassword(updateModel);
    }

    @ApiOperation(value = "token注销")
    @ApiImplicitParam(name = "Authorization", paramType = "header")
    @GetMapping(value = "/oauth/revoke-token")
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult<Boolean> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            return new ResponseResult<>();
        }
        return new ResponseResult<>(false).setCode(CommonConstants.NOT_FOUND).setMsg("注销失败");
    }

    @PostMapping("/updatePasswordByUsername")
    public ResponseResult<Boolean> updatePassword1(@RequestBody UpdatePasswordDO passwordDO, BindingResult bindingResult) {
        ResponseResult validate = ValidateUtils.validate(bindingResult);
        if (validate.getCode() != CommonConstants.SUCCESS) {
            return validate;
        }
        return manageService.updatePassword1(passwordDO);
    }

    @GetMapping("/deleteUser")
    public ResponseResult<Boolean> deleteUser(@RequestParam String userName) {
        return manageService.deleteUser(userName);
    }

    /**
     * 判断用户是否存在
     *
     * @param userName
     * @return
     */
    @GetMapping("/exist")
    public ResponseResult<Boolean> exist(@RequestParam String userName) {
        return manageService.exist(userName);
    }
}
