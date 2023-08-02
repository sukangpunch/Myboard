package com.springboot.board.service;

import com.springboot.board.dto.SignInResultDto;
import com.springboot.board.dto.SignUpResultDto;

public interface SignService {

    SignUpResultDto signUp(String id, String password, String name,String email, String role);

    SignInResultDto signIn(String id, String password) throws RuntimeException;

}
