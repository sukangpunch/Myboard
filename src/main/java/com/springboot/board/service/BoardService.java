package com.springboot.board.service;

import com.springboot.board.domain.Board;
import com.springboot.board.domain.User;
import com.springboot.board.dto.BoardDto;
import com.springboot.board.dto.BoardResponseDto;
import com.springboot.board.repository.BoardRepository;
import com.springboot.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 5; // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 4; // 한 페이지에 존재하는 게시글 수

    //Entity -> Dto로 변환
    //Builder 패턴으로 Entity를 Dto로 변환해주는 Method이다.
    private BoardResponseDto convertEntityToDto(Board board){
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }


    //repository의 find() 관련 메서드를 호출할 때 Pageable 인터페이스를 구현한 Class(PageRequest.of())를 전달하면 Paging을 할 수 있다.

    //첫 번째와 두 번째 인자로 page와 size를 전달하고, 세 번째 인자로 정렬 방식을 결정하였다.
    //(createdDate 기준으로 오름차순 할 수 있도록 설정한 부분이다.)
    //반환된 Page 객체의 getContent() 메서드를 호출하면, Entity를 List 형태로 꺼내올 수 있다. 이를 DTO로 Controller에게 전달하게 된다.
   // 트랜잭션은 데이터베이스 작업을 묶어서 원자적으로 처리하고, 중간에 오류가 발생하면 롤백하여 데이터 일관성을 보장하는데 사용됩니다.
    public List<BoardResponseDto> getBoardlist(Integer pageNum){
        Page<Board> page = boardRepository.findAll(PageRequest.of(
                pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC,"createdDate")));

        List<Board> boardEntities = page.getContent();
        List<BoardResponseDto> boardDtoList = new ArrayList<>();

        for(Board board : boardEntities){
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    //boardRepository의 findById(id) 메서드로 board 게시글 내용을 가져온 뒤
    //-> builder() 메서드를 활용하여 boardDTO 객체로 만들고
    //-> boadrDTO를 리턴 밸류로 전달해준다.

    public BoardResponseDto getPost(Long id){
        // Optional : NPE(NullPointerException) 방지
         Board board = boardRepository.findById(id).orElseThrow(() -> new NullPointerException("게시물이 존재하지 않습니다."));

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .id(board.getId())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .modifiedDate(board.getModifiedDate())
                .createdDate(board.getCreatedDate())
                .build();

        return boardResponseDto;
    }


    //boardRepository의 save 메서드를 사용하여 데이터를 저장한다.
    //그 뒤에 getter를 활용하여 Id를 받아오고 return 밸류를 전달해준다.

    public Long savePost(Long userId,BoardDto boardDto){

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 회원입니다"));
        Board board = boardDto.toEntity();
        board.setCreatedDate(LocalDateTime.now());
        board.setUser(user);

        return boardRepository.save(board).getId();
    }


    public BoardResponseDto updateBoard(Long no, BoardDto boardDto) {
        // 데이터베이스에서 no에 해당하는 board 엔티티를 조회
        Board board = boardRepository.findById(no)
                .orElseThrow(() -> new NoSuchElementException("Board not found with ID: " + no));

        // board 엔티티의 필드를 boardDto 값으로 업데이트하고 데이터베이스에 저장
        board.setWriter(boardDto.getWriter());
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setModifiedDate(LocalDateTime.now());
        boardRepository.save(board);

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .id(board.getId())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();

        return boardResponseDto;
    }

    //게시글 삭제
    public void deletePost(Long id){
        boardRepository.deleteById(id);
    }

    //검색 API
    //사용자가 Front에서 keyword를 검색하면 Controller로부터 keyword를 전달받게 된다.
    //이 Keyword가 Entity 내에 있는지 확인하는 Method이다.
    //있을 경우, boardEntities를 for loop 돌아서 boardDtoList에 Element를 추가한 뒤 boardDtoList를 Controller에게 전달해주고, 없을 경우 빈 Array를 전달해준다.


    public List<BoardResponseDto> searchPosts(String keyword){
        List<Board> boardEntities = boardRepository.findByTitleContaining(keyword);
        List<BoardResponseDto> boardDtoList = new ArrayList<>();

        if(boardEntities.isEmpty()) return boardDtoList;

        for(Board board : boardEntities){
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

}
