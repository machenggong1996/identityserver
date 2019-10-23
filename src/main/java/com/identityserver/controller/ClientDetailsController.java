package com.identityserver.controller;

import com.identityserver.service.ClientServiceImpl;
import com.identityserver.util.CommonConstants;
import com.identityserver.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by mcg on 2019/8/16.
 */
@RestController
@Api(value = "客户端注册")
@RequestMapping(value = "client")
public class ClientDetailsController {

    @Autowired
    ClientServiceImpl jdbcClientDetailsService;

    @PostMapping(value = "addClient")
    @ApiOperation(value = "客户端注册")
    public ResponseResult<Boolean> addClient(@RequestBody BaseClientDetails baseClientDetails){
        baseClientDetails.setClientId(UUID.randomUUID().toString());
        //密码编码
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        baseClientDetails.setClientSecret(bcryptPasswordEncoder.encode(baseClientDetails.getClientSecret()));
        try {
            jdbcClientDetailsService.addClientDetails(baseClientDetails);
        }catch (Exception e){
            new ResponseResult<>(false).setCode(CommonConstants.INVALID_INPUT).setMsg("注册失败");
        }
        return new ResponseResult<>();
    }

}
