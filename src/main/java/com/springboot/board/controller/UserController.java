package com.springboot.board.controller;

import com.springboot.board.dto.BoardDto;
import com.springboot.board.dto.UserDto;
import com.springboot.board.dto.UserResponseDto;
import com.springboot.board.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"유저 정보"}, description = "유저 생성,조회,수정,삭제")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value="사용자 계정 조회", notes = "사용자 계정을 조회합니다.")
    @GetMapping("/info/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.getUser(userId);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value="사용자 계정 생성", notes = "사용자 계정을 생성합니다.")
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserDto userDto) {
        UserResponseDto userResponseDto = userService.createUser(userDto);

        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @ApiOperation(value="사용자 계정 수정", notes = "사용자 계정을 수정합니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        UserResponseDto userResponseDto = userService.updateUser(userId, userDto);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value="사용자 계정 삭제", notes = "사용자 계정을 삭제합니다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value ="사용자 번호로 게시물 조회", notes = "사용자 번호로 사용자가 작성한 게시글들을 조회 할 수 있습니다.")
    @GetMapping({"/{userId}"})
    public ResponseEntity<List<BoardDto>> searchBoard(@PathVariable("userId") Long userId){
        List<BoardDto> userDtoList = userService.getBoardsByUserId(userId);

        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }


}
