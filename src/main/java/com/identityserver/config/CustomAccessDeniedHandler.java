package com.identityserver.config;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shanshu ()
 * @date 2020/05/18
 */

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        Map<String, Object> responseMessage = new HashMap<>(16);
        responseMessage.put("status", HttpStatus.FORBIDDEN.value());
        responseMessage.put("message", "权限不足！");
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(httpServletResponse.getOutputStream(),
                                                                                JsonEncoding.UTF8);
        objectMapper.writeValue(jsonGenerator, responseMessage);
    }
}
