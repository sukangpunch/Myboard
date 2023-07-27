package com.springboot.board.service;

import com.springboot.board.domain.Board;
import com.springboot.board.domain.User;
import com.springboot.board.dto.BoardDto;
import com.springboot.board.repository.BoardRepository;
import com.springboot.board.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private BoardDto convertEntityToDto(Board board){
        return BoardDto.builder()
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
    @Transactional // 트랜잭션은 데이터베이스 작업을 묶어서 원자적으로 처리하고, 중간에 오류가 발생하면 롤백하여 데이터 일관성을 보장하는데 사용됩니다.
    public List<BoardDto> getBoardlist(Integer pageNum){
        Page<Board> page = boardRepository.findAll(PageRequest.of(
                pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC,"createdDate")));

        List<Board> boardEntities = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for(Board board : boardEntities){
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    //boardRepository의 findById(id) 메서드로 board 게시글 내용을 가져온 뒤
    //-> builder() 메서드를 활용하여 boardDTO 객체로 만들고
    //-> boadrDTO를 리턴 밸류로 전달해준다.
    @Transactional
    public BoardDto getPost(Long id){
        // Optional : NPE(NullPointerException) 방지
         Board board = boardRepository.findById(id).orElseThrow(() -> new NullPointerException("null이다"));

        BoardDto boardDto = BoardDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();

        return boardDto;
    }


    //boardRepository의 save 메서드를 사용하여 데이터를 저장한다.
    //그 뒤에 getter를 활용하여 Id를 받아오고 return 밸류를 전달해준다.
    @Transactional
    public Long savePost(Long userId,BoardDto boardDto){

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 회원입니다"));
        Board board = boardDto.toEntity();
        board.setUser(user);

        return boardRepository.save(board).getId();
    }


    public Board updateBoard(Long no, BoardDto boardDto) {
        // 데이터베이스에서 no에 해당하는 board 엔티티를 조회
        Board board = boardRepository.findById(no)
                .orElseThrow(() -> new NoSuchElementException("Board not found with ID: " + no));

        // board 엔티티의 필드를 boardDto 값으로 업데이트하고 데이터베이스에 저장
        board.setWriter(boardDto.getWriter());
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());

        return boardRepository.save(board);
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long id){
        boardRepository.deleteById(id);
    }

    //검색 API
    //사용자가 Front에서 keyword를 검색하면 Controller로부터 keyword를 전달받게 된다.
    //이 Keyword가 Entity 내에 있는지 확인하는 Method이다.
    //있을 경우, boardEntities를 for loop 돌아서 boardDtoList에 Element를 추가한 뒤 boardDtoList를 Controller에게 전달해주고, 없을 경우 빈 Array를 전달해준다.

    @Transactional
    public List<BoardDto> searchPosts(String keyword){
        List<Board> boardEntities = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if(boardEntities.isEmpty()) return boardDtoList;

        for(Board board : boardEntities){
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    //페이징
    //전체 게시글 개수를 가져온다.
    @Transactional
    public Long getBoardCount(){
        return boardRepository.count();
    }

    //Integer 사용 이유
    //Wrapper 클래스(객체)
    //null값을 처리할 수 있다.
    //null값을 처리할 수 있기 때문에 SQL과 연동할 경우 처리가 용이하다.
    //DB에서 자료형이 정수형이지만 null값이 필요할 경우 사용된다.

   
    //나중에 view 나타낼때 사용
//    public Integer[] getPageList(Integer curPageNum) {
//        // 페이지 목록을 저장할 Integer 배열을 생성합니다.
//        // BLOCK_PAGE_NUM_COUNT는 한 번에 보여줄 페이지 번호의 개수를 나타냅니다.
//        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
//
//
//        // 총 게시물의 개수를 가져오는 getBoardCount() 메서드를 호출하여
//        // 총 게시물 갯수를 Double 형태로 저장합니다.
//        Double postsTotalCount = Double.valueOf(this.getBoardCount());
//
//        // 총 게시물을 한 페이지에 표시되는 게시물 개수로 나누어 총 페이지 수를 계산합니다.
//        // Math.ceil() 함수를 사용하여 소수점 이하를 올림 처리하여 마지막 페이지 번호를 구합니다.
//        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));
//
//        //현재 페이지를 기준으로 표시될 블럭의 마지막 페이지 번호를 계산합니다.
//        // totalLastPageNum과 curPageNum + BLOCK_PAGE_NUM_COUNT 중 작은 값을 선택합니다.
//        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
//                ? curPageNum + BLOCK_PAGE_NUM_COUNT
//                : totalLastPageNum;
//
//        // 현재 페이지 번호가 3 이하인 경우 1로 조정하고, 그렇지 않으면 현재 페이지 번호에서 2를 뺍니다.
//        // 이 조정은 페이지 목록이 1부터 시작하도록 하고, 현재 페이지가 가운데에 위치하도록 합니다.
//        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;
//
//        //페이지 목록 배열에 페이지 번호를 할당합니다.
//        // curPageNum부터 blockLastPageNum까지의 숫자를 배열에 순서대로 저장합니다.
//        for (int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
//            pageList[idx] = val;
//        }
//        return pageList;
//    }

    //이렇게 하면 현재 페이지를 기준으로 한 페이지 블럭의 페이지 목록이 생성되고,
    // 이를 UI에서 이용하여 페이지 네비게이션을 표시할 수 있게 됩니다.
    // 예를 들어, getPageList(5)를 호출하면
    // 현재 페이지를 5로 기준으로 한 페이지 블럭의 페이지 목록이 반환됩니다.

}
