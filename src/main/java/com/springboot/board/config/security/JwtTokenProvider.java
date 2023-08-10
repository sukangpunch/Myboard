package com.springboot.board.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

//애플리케이션이 가동되면서 빈이 자동으로 주입.
//입력한 로그인 정보와 DB를 비교, Spring Security의 AuthenticationProvider을 구현한 클래스로
// security-context에 provider로 등록 후 인증절차를 구현한다.
@Component //개발자가 직접 작성한 코드에 있는 의존성을 주입받기 위해서 사용하기에 적절하다.
@RequiredArgsConstructor
public class JwtTokenProvider {
        private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
        private final UserDetailsService userDetailsService;

        // Spring 애플리케이션의 설정 파일 (예: application.properties 또는 application.yml)에서 정의된 값을 가져와서 해당 필드에 주입(injection)
        @Value("{springboot.jwt.secret}")
        private String secretKey = "secretKey";
        private final long tokenValidMillisecond = 1000L *60*60;


        //해당 객체가 빈 객체로 주입된 이후 수행되는 메서드를 가리킴.
        @PostConstruct
        protected void init(){
            LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
            secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
            
            LOGGER.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
        }
        
        public String createToken(String userUid, List<String> roles){
            LOGGER.info("[createToken] 토큰 생성 시작");
            //JWT 토큰의 내용에 값을 넣기 위한 Claims 객체.
            //sub는 토큰의 주제를 나타냄(식별자)
            Claims claims = Jwts.claims().setSubject(userUid);
            claims.put("roles",roles);
            Date now = new Date();

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime()+tokenValidMillisecond))
                    .signWith(SignatureAlgorithm.HS256,secretKey)
                    .compact();
            
            LOGGER.info("[createToken] 토큰 생성 완료");
            return token;
        }

        //필터에서 인증이 성공했을 때 SecurityContextHolder에 저장할 Authentication을 생성하는 역할.
        public Authentication getAuthentication(String token){
            LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 시작");
            //초기화를 위한 UserDetails가 필요함.
            UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
            LOGGER.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}",userDetails.getUsername());

            //Authentication 형식으로 리턴
            return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
        }

        public String getUsername(String token){
            LOGGER.info("[getUsername] 토큰 기반 회원 구별 정보 추출");
            //Jwts.parser()를 통해 secretKey를 설정하고 클레임을 추출해서 토큰을 생성할 때 넣었던 sub 값을 추출
            //이 정보를 기반으로 UserDetails 객체 생성.
            String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            LOGGER.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}",info);
            return info;
        }

        //HttpServletRequest를 파라미터로 받아 헤더값으로 전달된 'X-AUTH-TOKEN' 값을 가져와 리턴
        public String resolveToken(HttpServletRequest request){
            LOGGER.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
            return request.getHeader("JWT");
        }

        //토큰을 전달받아 클레임의 유효기간 체크
        public boolean validateToken(String token){
            LOGGER.info("[validateToken] 토큰 유효 체크 시작");
            try{
                Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

                return !claims.getBody().getExpiration().before(new Date());
            }catch (Exception e){
                LOGGER.info("[validateToken] 토큰 유효 체크 예외 발생");
                return false;
            }
        }

}
