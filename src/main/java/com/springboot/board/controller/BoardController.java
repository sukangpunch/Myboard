package com.springboot.board.controller;

import com.springboot.board.domain.User;
import com.springboot.board.dto.BoardDto;
import com.springboot.board.dto.BoardResponseDto;
import com.springboot.board.repository.UserRepository;
import com.springboot.board.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"게시글 정보"}, description = "게시글 생성,조회,수정,삭제")
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final Logger LOGGER = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;
    private final UserRepository userRepository;

    @ApiOperation(value="페이지 번호로 게시물 조회", notes= "해당 페이지 내의 게시물을 전부 조회합니다.")
    @GetMapping("list/{page}")
    public ResponseEntity<List<BoardResponseDto>> list(@RequestParam(value = "page", defaultValue = "1") Integer pageNum) throws RuntimeException {
        LOGGER.info("[BoardGet]페이지 번호로 게시물을 조회합니다. 페이지 번호 : {}",pageNum);
        List<BoardResponseDto> boardList = boardService.getBoardlist(pageNum);

        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }

    // 게시물 상세 페이지이며, {no}로 페이지 넘버를 받는다.
    // PathVariable 애노테이션을 통해 no를 받음
    @ApiOperation(value="게시물 번호로 조회", notes = "게시물의 번호로 게시물을 조회 할 수 있습니다..")
    @GetMapping("/get/{no}")
    public ResponseEntity<BoardResponseDto> detail(@PathVariable("no") Long no) throws RuntimeException{
        LOGGER.info("[BoardGet]게시물 번호로 게시물을 조회합니다. 게시물 번호 : {}",no);
        BoardResponseDto boardResponseDto = boardService.getPost(no);

        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);
    }


    //글을 쓴 뒤 POST 메서드로 DB에 저장하고 그 후에 특정 경로로 리다이렉션 해줄것
    @ApiOperation(value="게시물 작성", notes = "게시물을 작성할 수 있습니다..")
    @PostMapping("/post")
    public ResponseEntity<Void> write(@Valid @RequestBody BoardDto boardDto) {
        //헤더에 담긴 토큰을 기반으로 현재 인증된 사용자의 정보를 가져옴,
        //사용자의 이름(id),비밀번호,권한 등 정보를 담고있음.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //실제 사용자 정보를 추출, 인증주체(principal)을 가져오고 주체는 UserDetails객체로 캐스팅됨
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            //userDetails객체에 담겨있는 uid(로그인아이디)를 식별자로 가져옴
            String uid = userDetails.getUsername(); // 로그인한 사용자의 이름 (username)
            //uid를 이용하여 데이터베이스에서 현재 사용자에 대한 추가 정보(유저 엔티티)를 가져옴
            User user = userRepository.getByUid(uid);

            LOGGER.info("[BoardPost]게시물을 작성합니다. 유저 Id : {}",user.getUid());
            //현재 사용자의 인덱스 아이디와 게시물 데이터를 전달하여 게시물 저장.
            boardService.savePost(user.getId(), boardDto);

            return new ResponseEntity<>(HttpStatus.CREATED);
    }


    //수정 후 데이터베이스에 저장하는 메서드
    @ApiOperation(value="게시물 수정",notes = "게시물번호로 게시물을 수정할 수 있습니다.")
    @PutMapping("/update/{no}")
    public ResponseEntity<BoardResponseDto> update(@PathVariable("no") Long no, @RequestBody BoardDto boardDto) throws RuntimeException{
        LOGGER.info("[BoardUpdate]게시물을 수정합니다. 게시글 번호 : {}",no);
        BoardResponseDto boardResponseDto = boardService.updateBoard(no,boardDto);

        return new ResponseEntity<>(boardResponseDto,HttpStatus.OK);
    }

    //삭제 메서드
    @ApiOperation(value = "게시물 삭제", notes = "게시물 번호로 게시물을 삭제할 수 있습니다.")
    @DeleteMapping("/delete/{no}")
    public ResponseEntity<Void> delete(@PathVariable("no") Long no) throws RuntimeException{
        LOGGER.info("[BoardDelete]게시물을 삭제합니다. 게시글 번호 : {}",no);
        boardService.deletePost(no);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 검색
    // keyword를 전달 받고
    // List 형식으로 keyword값에 해당하는 게시물을 가져온다.
    @ApiOperation(value="게시물 키워드로 조회", notes = "게시물 제목으로 조회할 수 있습니다..")
    @GetMapping("/search")
    public ResponseEntity<List<BoardResponseDto>> search(@RequestParam(value = "keyword") String keyword) throws RuntimeException {
        LOGGER.info("[BoardGet]게시물을 키워드로 조회합니다. . 키워드 : {}",keyword);
        List<BoardResponseDto> boardDtoList = boardService.searchPosts(keyword);

        return new ResponseEntity<>(boardDtoList, HttpStatus.OK);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String,String>> ExceptionHandler(RuntimeException e){
        HttpHeaders responseHeaders = new HttpHeaders();

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        LOGGER.error("ExceptionHandler 호출, {}, {}",e.getCause(),e.getMessage());

        Map<String,String> map = new HashMap<>();
        map.put("error type",httpStatus.getReasonPhrase());
        map.put("code","404");
        map.put("message","정보를 찾지 못했습니다.");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }

}
