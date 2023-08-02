package com.springboot.board.dto;

import com.springboot.board.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class BoardResponseDto {
    private Long id;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public BoardResponseDto(Long id, String title, String content,String writer, LocalDateTime createdDate, LocalDateTime modifiedDate ){
        this.id=id;
        this.writer=writer;
        this.title=title;
        this.content = content;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;
    }


}
