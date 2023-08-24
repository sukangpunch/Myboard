package com.springboot.board.controller;

import com.springboot.board.domain.User;
import com.springboot.board.dto.*;
import com.springboot.board.repository.UserRepository;
import com.springboot.board.service.SignService;
import com.springboot.board.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"유저 정보"}, description = "유저 회원가입, 로그인, 조회, 수정, 삭제")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final SignService signService;

    private final UserRepository userRepository;


    @ApiOperation(value="사용자 계정 조회", notes = "사용자 계정을 조회합니다.")
    @GetMapping("/info")
    public ResponseEntity<UserResponseDto> getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String uid = userDetails.getUsername(); // 로그인한 사용자의 이름 (username)
            User user = userRepository.getByUid(uid);

            LOGGER.info("[UserPost]유저 정보를 조회합니다. 유저 Id : {}",user.getUid());

            UserResponseDto userResponseDto = userService.getUser(user.getId());

            return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
    }


    @ApiOperation(value="사용자 계정 수정", notes = "사용자 계정을 수정합니다.")
    @PutMapping("/updateUser")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserDto userDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String uid = userDetails.getUsername(); // 로그인한 사용자의 이름 (username)
            User user = userRepository.getByUid(uid);

            LOGGER.info("[UserUpdate]유저 정보를 수정합니다. 유저 Id : {}",user.getUid());

            UserResponseDto userResponseDto = userService.updateUser(user.getId(), userDto);

            return new ResponseEntity<>(userResponseDto,HttpStatus.OK);

    }

    @ApiOperation(value="사용자 계정 삭제", notes = "사용자 계정을 삭제합니다.")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(
            @ApiParam(value = "ID", required = true) @RequestParam String id,
            @ApiParam(value = "Password", required = true) @RequestParam String password) throws RuntimeException{
        LOGGER.info("[UserDelete]유저 계정을 삭제합니다. Id : {}, password : **** ",id);

        boolean deleted = userService.deleteUser(id,password);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value ="사용자 게시물 조회", notes = "사용자가 작성한 게시글들을 조회 할 수 있습니다.")
    @GetMapping({"/searchBoard"})
    public ResponseEntity<List<BoardDto>> searchBoard(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String uid = userDetails.getUsername(); // 로그인한 사용자의 이름 (username)
            User user = userRepository.getByUid(uid);

            LOGGER.info("[UserUpdate]유저 정보를 수정합니다. 유저 Id : {}",user.getUid());

            List<BoardDto> userDtoList = userService.getBoardsByUserId(user.getId());

            return new ResponseEntity<>(userDtoList,HttpStatus.OK);
    }

    @ApiOperation(value="로그인",notes = "아이디와 비밀번호를 입력하세요")
    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(@Valid @RequestBody SignInRequestDto requestDto) throws RuntimeException{
        LOGGER.info("[signIn] 로그인을 시도하고 있습니다. id : {], pw: ****",requestDto.getId());
        SignInResultDto signInResultDto = signService.signIn(requestDto.getId(),requestDto.getPassword());

        if(signInResultDto.getCode() == 0){
            LOGGER.info("[signIn] 정상적으로 로그인 되었습니다. id : {}, token : {}",requestDto.getId(),signInResultDto.getToken());
        }

        return signInResultDto;
    }

    @ApiOperation(value="회원가입",notes = "아이디, 비밀번호, 이메일, 권한을 입력하세요")
    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(@Valid @RequestBody SignUpRequestDto requestDto){
        LOGGER.info("[signUp] 회원가입을 수행합니다. id: {},password: ****, name : {}, email : {},  role : {}",requestDto.getId(),requestDto.getName(),requestDto.getEmail(),requestDto.getRole());
        SignUpResultDto signUpResultDto = signService.signUp(requestDto.getId(),requestDto.getPassword(),requestDto.getName(),requestDto.getEmail(),requestDto.getRole());

        LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}", requestDto.getId());

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
        map.put("message","잘못된 접근입니다.");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}
