package com.springboot.board.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode
@Table(name="board_tb")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    @ToString.Exclude
    private User user;

    @Builder
    public Board(Long id, String title, String content, String writer)
    {
        this.id =id;
        this.title=title;
        this.writer=writer;
        this.content = content;
    }

}
