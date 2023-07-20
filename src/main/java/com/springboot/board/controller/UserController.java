package com.springboot.board.controller;

import com.springboot.board.domain.User;
import com.springboot.board.dto.UserDto;
import com.springboot.board.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @ApiOperation(value="사용자 계정 조회", notes = "사용자 계정을 조회합니다.")
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = userService.getUser(userId);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value="사용자 계정 생성", notes = "사용자 계정을 생성합니다.")
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userDto);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @ApiOperation(value="사용자 계정 수정", notes = "사용자 계정을 수정합니다.")
    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        User user = userService.updateUser(userId, userDto);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value="사용자 계정 삭제", notes = "사용자 계정을 삭제합니다.")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);

        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
