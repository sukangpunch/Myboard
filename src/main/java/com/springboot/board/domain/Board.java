package com.springboot.board.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="board")
public class Board extends Time{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Board(Long id, String title, String content, String writer, User user)
    {

        //Assert 구문으로 안전한 객체 생성 패턴을 구현 writer, title, content 객체가 전달되지 않을 경우 Exception을 발생시킨다.
        Assert.hasText(writer, "writer must not be empty");
        Assert.hasText(title, "title must not be empty");
        Assert.hasText(content, "content must not be empty");

        this.id =id;
        this.title=title;
        this.writer=writer;
        this.content = content;
        this.user=user;
    }

}
