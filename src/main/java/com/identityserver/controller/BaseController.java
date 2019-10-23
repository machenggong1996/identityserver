package com.identityserver.controller;

import com.identityserver.pojo.CommonResponse;

public abstract class BaseController {
    protected CommonResponse success(){
        CommonResponse commonResponse =new CommonResponse();
        commonResponse.setCode("200");
        commonResponse.setMessgae("OK");
        return commonResponse;
    }

    protected CommonResponse failedWithMessage(String message){
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode("500");
        commonResponse.setMessgae(message);
        return commonResponse;
    }
}
