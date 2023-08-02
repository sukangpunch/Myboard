package com.springboot.board.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.httpBasic().disable()
                //사이트간 요청 위조 보안 CSRF
                .csrf().disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/sign-api/sign-in","/sign-api/sign-up","/sign-api/exception","/swagger-resources/**","/swagger-ui/index.html",
                        "/webjars/**","/swagger/**","/sign-api/exception","/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET,"/boards/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")

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
