package com.springboot.board.controller;

import com.springboot.board.domain.Board;
import com.springboot.board.dto.BoardDto;
import com.springboot.board.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("board")
@AllArgsConstructor

public class BoardController {


    private BoardService boardService;


    // 게시판

    // 게시글 목록
    // list 경로로 GET 메서드 요청이 들어올 경우 list 메서드와 맵핑시킴
    // list 경로에 요청 파라미터가 있을 경우 (?page=1), 그에 따른 페이지네이션을 수행함.

    //기본 URL과 "/list"라는 경로로 들어오는 GET 요청을 모두 처리할 수 있다.

    @ApiOperation(value="페이지 번호로 게시물 조회", notes= "후에 board/ 경로와 board/list 경로에서 조회 할 수 있게 하기 위함")
    @GetMapping({"", "/list"})
    public ResponseEntity<List<BoardDto>> list(@RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        List<BoardDto> boardList = boardService.getBoardlist(pageNum);

        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }

    // 게시물 상세 페이지이며, {no}로 페이지 넘버를 받는다.
    // PathVariable 애노테이션을 통해 no를 받음
    @ApiOperation(value="게시물 상세 조회", notes = "게시물의 번호로 정보를 상세 조회 할 수 있게 함.")
    @GetMapping("/post/{no}")
    public ResponseEntity<BoardDto> detail(@PathVariable("no") Long no) {
        BoardDto boardDto = boardService.getPost(no);

        return new ResponseEntity<>(boardDto, HttpStatus.OK);
    }

    //      후에 글쓰기 페이지 구현, html로 로그인페이지를 구현 하고, post로 넘겨줄 것임
    //    @GetMapping("/post")
    //    public String write() {
    //       return "board/write";
    //    }


    //글을 쓴 뒤 POST 메서드로 DB에 저장하고 그 후에 특정 경로로 리다이렉션 해줄것
    @ApiOperation(value="게시물 작성", notes = "게시물을 작성할 수 있다.")
    @PostMapping("/post")
    public ResponseEntity<Void> write(@RequestBody BoardDto boardDto) {
        boardService.savePost(boardDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // 후에 사용할 수정 페이지를 위함.
    // edit 메소드는 특정 게시물을 조회하여 수정하기 위한 페이지로 이동하고,
    // @PutMapping 어노테이션이 사용된 update 메소드는 수정된 게시물 정보를 받아서 데이터베이스에 저장하는 역할을 합니다.
//    @GetMapping("/post/edit/{no}")
//    public String edit(@PathVariable("no") Long no, Model model) {
//        BoardDto boardDTO = boardService.getPost(no);
//
//        model.addAttribute("boardDto", boardDTO);
//        return "board/update";
//    }

    //수정 후 데이터베이스에 저장하는 메서드
    @ApiOperation(value="게시물 수정",notes = "게시물을 수정함")
    @PutMapping("/post/edit/{no}")
    public ResponseEntity<Board> update(@PathVariable("no") Long no, @RequestBody BoardDto boardDto) {
        Board board = boardService.updateBoard(no,boardDto);

        return new ResponseEntity<>(board,HttpStatus.OK);
    }

    //삭제 메서드
    @ApiOperation(value = "게시물 삭제", notes = "게시물 번호로 게시물 삭제")
    @DeleteMapping("/post/{no}")
    public ResponseEntity<Void> delete(@PathVariable("no") Long no) {
        boardService.deletePost(no);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 검색
    // keyword를 전달 받고
    // List 형식으로 keyword값에 해당하는 게시물을 가져온다.
    @ApiOperation(value="게시물 키워드로 조회", notes = "게시물 제목으로 조회할 수 있다.")
    @GetMapping("/board/search")
    public ResponseEntity<List<BoardDto>> search(@RequestParam(value = "keyword") String keyword) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);

        return new ResponseEntity<>(boardDtoList, HttpStatus.OK);
    }

}