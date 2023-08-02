//package com.springboot.board.repository;
//
//import com.springboot.board.domain.Board;
//import com.springboot.board.domain.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//public class UserRepositoryTest {
//
//    @Autowired
//    BoardRepository boardRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Test
//    @Transactional
//    void relationshipTest(){
//
//        User user = new User();
//        user.setName("강형준");
//        user.setEmail("popora99@naver.com");
//
//        userRepository.save(user);
//
//        Board board1 = new Board();
//        board1.setTitle("안녕하세요 여러분");
//        board1.setWriter("강형준");
//        board1.setContent("첫글입니다~~");
//        board1.setUser(user);
//
//        Board board2 = new Board();
//        board2.setTitle("별명으로 첫글입니다앙~");
//        board2.setWriter("주먹대장");
//        board2.setContent("히히코모리");
//        board2.setUser(user);
//
//        boardRepository.save(board1);
//        boardRepository.save(board2);
//
//        List<Board> boards =userRepository.findById(user.getId()).get().getBoardList();
//
//        for(Board board : boards){
//            System.out.println(board);
//        }
//
//    }
//}
