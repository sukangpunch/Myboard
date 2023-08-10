package com.springboot.board.repository;

import com.springboot.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    //현제 id값은 인덱스 값이기 때문에, 토큰생성을 위해 uid를 추출하는 메서드
    User getByUid(String uid);

}
