package com.springboot.board.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//JwtAuthenticationFilter는 JWT 토큰으로 인증하고 SecurityContextHolder에 추가하는 필터를 설정하는 클래스
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //JwtTokenProvider을 통해 servletReuqest에서 토큰을 추출하고,
    //토큰에 대한 유효성 검사. 토큰이 유효하다면 Authentication 객체를 생성해서 SecurityContextHolder에 추가.
    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                                    FilterChain filterChain) throws ServletException, IOException{
        String token = jwtTokenProvider.resolveToken(servletRequest);
        LOGGER.info("[doFilterInternal] token 값 추출 완료. token : {}",token);
        
        LOGGER.info("[doFilterInternal] token 값 유효성 체크 시작");
        if(token !=null && jwtTokenProvider.validateToken(token)){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            //SecurityContextHolder는  Spring Security에서 현재 스레드의 보안 컨텍스트를 관리하는 클래스입니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOGGER.info("[doFilterInternal] token 값 유효성 체크 완료");
        }
        //doFilter 기준으로 앞에 작성한 코드는 서블릿이 실행되기 전에 실행, 뒤에 작성한 코드는 서블릿이 실행된 후 실행
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
