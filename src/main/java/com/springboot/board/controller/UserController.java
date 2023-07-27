package com.springboot.board.controller;

import com.springboot.board.domain.User;
import com.springboot.board.dto.BoardDto;
import com.springboot.board.dto.UserDto;
import com.springboot.board.dto.UserResponseDto;
import com.springboot.board.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userDto);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @ApiOperation(value="사용자 계정 수정", notes = "사용자 계정을 수정합니다.")
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        User user = userService.updateUser(userId, userDto);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value="사용자 계정 삭제", notes = "사용자 계정을 삭제합니다.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value ="사용자 번호로 조회", notes = "사용자 번호로 사용자가 작성한 게시글 조회 할 수 있다.")
    @GetMapping({"/search/{userNum}"})
    public ResponseEntity<List<BoardDto>> searchBoard(@PathVariable("userNum") Long userNum){
        List<BoardDto> userDtoList = userService.getBoardsByUserId(userNum);

        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }


}
