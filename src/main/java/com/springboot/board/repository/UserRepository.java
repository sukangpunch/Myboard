package com.springboot.board.repository;

import com.springboot.board.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);//  email을 통해 이미 생성된 사용자인지 체크
}
