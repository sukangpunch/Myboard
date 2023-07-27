package com.springboot.board.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode
@Table(name = "user_tb")
@NoArgsConstructor // 외부에서의 생성을 열어 둘 필요가 없을 때 / 보안적으로 권장된다
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ToString.Exclude
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Board> boardList = new ArrayList<>();


    public void addBoard(Board board)
    {
        this.boardList.add(board);
    }

    public void removeBoard(Board board)
    {
        this.boardList.remove(board);
    }

    @Builder
    public User(String name, String email){
        this.name = name;
        this.email =email;
    }


}
