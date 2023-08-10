package com.springboot.board.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//설정 파일을 만들기 위한 어노테이션 or Bean을 등록하기 위한 어노테이션
//싱글톤으로 등록되어 단 1번만 출력
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Spring Security의 필터 체인 초기화 단계에서 호출됩니다.
    // 일반적으로 Spring Security가 시작되면 보안 설정이 초기화되고,
    // 이 메서드가 호출되어 웹 보안 구성이 이루어진다.
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.httpBasic().disable()
                //사이트간 요청 위조 보안 CSRF
                .csrf().disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/users/sign-in","/users/sign-up","/users/exception","/swagger-resources/**","/swagger-ui/index.html",
                        "/webjars/**","/swagger/**","/users/exception","/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET,"/boards/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
                .antMatchers(HttpMethod.GET,"/users/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")

                .antMatchers("**exception**").permitAll()

                .anyRequest().hasRole("ADMIN")

                .and()
                //권한을 확인하는 과정에서 통과하지 못하는 예외가 발생할 경우 예외 전달
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                //인증 과정에서 예외가 발생할 경우 예외를 전달.
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                //스프링 시큐리티에서 인증을 처리하는 필터인 UsernamePasswordAuthenticationFilter 앞에 앞에서 생성한,
                //JwtAuthenticationFilter를 추가하겠다는 의미.
                //추가된 필터에서 인증이 정상적으로 처리되면 UsernamePasswordAuthenticationFilter는 자동으로 통과된다.
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    //webSecurity를 사용하는 configure()메서드
    //인증과 인가가 모두 적용되기 전에 동작하는 설정
    @Override
    public void configure(WebSecurity webSecurity){
        webSecurity.ignoring().antMatchers("/v3/api-docs","/swagger-resources/**","/swagger-ui/index.html","/webjars/**","/swagger/**","/sign-api/exception");
    }

}
