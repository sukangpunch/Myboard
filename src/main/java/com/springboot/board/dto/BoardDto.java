package com.springboot.board.dto;

import com.springboot.board.domain.Board;
import lombok.*;

import java.time.LocalDateTime;

//DTO : 데이터 전달 목적
//데이터를 캡슐화한 데이터 전달 객체

@Getter
@Setter
@ToString           //객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴
@NoArgsConstructor
public class BoardDto {
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    //1. @Builder 애노테이션으로 BoardDto 객체의 생성자 부분을 만들어주고
    //2. toEntity 메서드를 통해 Service -> Database(Entity)로 Datca(게시판 글 상세 정보)를 전달할 때 Dto를 통해서 전달한다.

    public Board toEntity(){
        Board board = Board.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .build();

        return board;
    }

    @Builder
    public BoardDto(String title, String content,String writer, LocalDateTime createdDate, LocalDateTime modifiedDate ){
        this.writer=writer;
        this.title=title;
        this.content = content;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;
    }
}
