package com.springboot.board.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//엑세스 권한이 없는 리소스에 접근할 때 발생하는 예외
//데이터베이스에 존재하지 않는 정보를 기반으로 로그인 시도할때 발생
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);


    @Override                                                                     //엑세스 권한이 없는 리소스에 접근할 경우 발생하는 예외               
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException{
        LOGGER.info("[handle] 접근이 막혔을 경우 경로 리다이렉트");
        //Usercontroller의 Exceptionhandler로 리다이렉트
        response.sendRedirect("/users/exception");
    }
}
