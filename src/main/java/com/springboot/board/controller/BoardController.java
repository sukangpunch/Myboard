package com.springboot.board.controller;

import com.springboot.board.domain.Board;
import com.springboot.board.dto.BoardDto;
import com.springboot.board.dto.BoardResponseDto;
import com.springboot.board.dto.UserDto;
import com.springboot.board.service.BoardService;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;


    @ApiOperation(value="페이지 번호로 게시물 조회", notes= "후에 board/ 경로와 board/list 경로에서 조회 할 수 있게 하기 위함")
    @GetMapping({ "/list"})
    public ResponseEntity<List<BoardDto>> list(@RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        List<BoardDto> boardList = boardService.getBoardlist(pageNum);

        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }

    // 게시물 상세 페이지이며, {no}로 페이지 넘버를 받는다.
    // PathVariable 애노테이션을 통해 no를 받음
    @ApiOperation(value="게시물 상세 조회", notes = "게시물의 번호로 정보를 상세 조회 할 수 있게 함.")
    @GetMapping("/post/{no}")
    public ResponseEntity<BoardResponseDto> detail(@PathVariable("no") Long no) {
        BoardResponseDto boardResponseDto = boardService.getPost(no);

        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);
    }


    //글을 쓴 뒤 POST 메서드로 DB에 저장하고 그 후에 특정 경로로 리다이렉션 해줄것
    @ApiOperation(value="게시물 작성", notes = "게시물을 작성할 수 있다.")
    @PostMapping("/post/{userId}")
    public ResponseEntity<Void> write(@PathVariable Long userId,@RequestBody BoardDto boardDto) {
        boardService.savePost(userId,boardDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


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
    @GetMapping("/search")
    public ResponseEntity<List<BoardDto>> search(@RequestParam(value = "keyword") String keyword) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);

        return new ResponseEntity<>(boardDtoList, HttpStatus.OK);
    }


}
