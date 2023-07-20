package com.springboot.board.repository;

import com.springboot.board.domain.Board;
import com.springboot.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//JpaRepository<Entity 클래스,pk타입>
//Interface는 '규격'으로의 역할을 한다.
//JpaRepository : Entity의 CRUD가 가능하도록 JpaRepository 인터페이스를 제공하며, 상속해주기만 하면 된다.

//JpaRepository<Entity 클래스, PK 타입>
//=> Entity Class가 Board이며, Primary Key의 Type은 Long임을 의미한다.
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByTitleContaining(String keyword);
    Board findByUser(User user);

}
