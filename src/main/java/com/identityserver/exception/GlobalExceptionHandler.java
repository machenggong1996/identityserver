package com.identityserver.exception;

import com.identityserver.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author machenggong
 * @date 2020/05/21
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public ResponseResult cExceptionHandler(HttpServletRequest req, Exception e) {
//        log.error("发生业务异常！原因是：{}", e.getMessage());
//        return ResponseResult.builder().code(500).msg(e.getMessage()).build();
//    }

}
