package com.springboot.board.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서의 생성을 열어 둘 필요가 없을 때 / 보안적으로 권장된다
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Builder
    public User(String name, String email){
        this.name = name;
        this.email =email;
    }

    public User update(String name){
        this.name = name;
        return this;
    }


}
