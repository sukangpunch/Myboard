package com.springboot.board.common;

//로그인 성공 정보를 가져와주는 클래스
public enum CommonResponse {

    SUCCESS(0,"Success");

    int code;
    String msg;

    CommonResponse(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }
}
