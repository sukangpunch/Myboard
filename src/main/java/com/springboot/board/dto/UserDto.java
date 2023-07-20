package com.springboot.board.dto;

import com.springboot.board.domain.User;
import lombok.*;

import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
public class UserDto {

    private String name;
    private String email;

    @Builder
    public UserDto(String name, String email){
            this.name=name;
            this.email=email;
    }


    //toEntity 메서드를 통해 Service -> Database(Entity)로 Data를 전달할 때 Dto를 통해서 전달한다.
    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .build();
    }
}