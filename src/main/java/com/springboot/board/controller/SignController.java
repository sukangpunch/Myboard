package com.springboot.board.controller;

import com.springboot.board.dto.SignInResultDto;
import com.springboot.board.dto.SignUpResultDto;
import com.springboot.board.service.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = {"회원가입,로그인 정보"}, description = "회원가입, 로그인")
@RestController
@RequestMapping("/sign-api")
public class SignController {

    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;

    @Autowired
    public SignController(SignService signService)
    {
        this.signService = signService;
    }

    @ApiOperation(value="로그인",notes = "아이디와 비밀번호를 입력하세요")
    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(
            @ApiParam(value = "ID", required = true) @RequestParam String id,
            @ApiParam(value = "Password", required = true) @RequestParam String password) throws RuntimeException{
        LOGGER.info("[signIn] 로그인을 시도하고 있습니다. id : {], pw: ****",id);
        SignInResultDto signInResultDto = signService.signIn(id,password);

        if(signInResultDto.getCode() == 0){
            LOGGER.info("[signIn] 정상적으로 로그인 되었습니다. id : {}, token : {}",id,signInResultDto.getToken());
        }

        return signInResultDto;
    }
    @ApiOperation(value="회원가입",notes = "아이디, 비밀번호, 이메일, 권한을 입력하세요")
    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(
            @ApiParam(value="ID",required = true) @RequestParam String id,
            @ApiParam(value="비밀번호",required = true) @RequestParam String password,
            @ApiParam(value="이름",required = true) @RequestParam String name,
            @ApiParam(value="이메일",required = true) @RequestParam String email,
            @ApiParam(value="권한",required = true) @RequestParam String role){
        LOGGER.info("[signUp] 회원가입을 수행합니다. id: {},password: ****, name : {}, email : {},  role : {}",id,name,email,role);
        SignUpResultDto signUpResultDto = signService.signUp(id,password,name,email,role);

        LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}", id);
        return signUpResultDto;
    }

    //@ExceptionHandler = 특정 예외가 발생했을 때 해당 예외를 처리하는 메서드
    //RuntimeException 이 발생하였을때 실행되는 메서드
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String,String>> ExceptionHandler(RuntimeException e){
        HttpHeaders responseHeaders = new HttpHeaders();

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOGGER.error("ExceptionHandler 호출, {}, {}",e.getCause(),e.getMessage());

        Map<String,String> map = new HashMap<>();
        map.put("error type",httpStatus.getReasonPhrase());
        map.put("code","400");
        map.put("message","에러 발생");

        return new ResponseEntity<>(map, responseHeaders,httpStatus);
    }
}
