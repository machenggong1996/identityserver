package com.identityserver.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

public class ValidateUtils {

    public static ResponseResult validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).collect(Collectors.toList())) {
                stringBuilder.append(s);
            }
            return new ResponseResult().setCode(CommonConstants.INVALID_INPUT).setMsg(stringBuilder.toString());
        }
        return new ResponseResult();
    }
}
