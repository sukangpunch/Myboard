package com.springboot.board.service;

import com.springboot.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
//리포지토리를 통해 User 엔티티의 id를 DB에서 가져오는 서비스
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    //UserDetails는 사용자의 정보를 담는 인터페이스
    //username은 각 사용자를 구분할 ID를 의미
    @Override
    public UserDetails loadUserByUsername(String username){
        LOGGER.info("[loadUserByUsername] loadUserByUsername 수행. username : {}",username);
        return userRepository.getByUid(username);
    }

}
