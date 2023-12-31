package com.springboot.board.service;

import com.springboot.board.domain.Board;
import com.springboot.board.domain.User;
import com.springboot.board.dto.BoardDto;
import com.springboot.board.dto.UserDto;
import com.springboot.board.dto.UserResponseDto;
import com.springboot.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없어"));

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserResponseDto updateUser(Long userId, UserDto userDto) {
        // 데이터베이스에서 userId에 해당하는 User 엔티티를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        // User 엔티티의 필드를 UserInputDto의 값으로 업데이트하고 데이터베이스에 저장
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        return userResponseDto;
    }

    public boolean deleteUser(String userId, String password) {
        User user = userRepository.getByUid(userId);

        if(!passwordEncoder.matches(password,user.getPassword())){
            return false;
        }
        else
        {
            userRepository.delete(user);
            return true;
        }
    }


    //LAZY 사용시 세션이 남아있지 않아서 오류가 뜨기 때문에 Transactional 사용
    @Transactional
    public List<BoardDto> getBoardsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        List<Board> boards = user.getBoardList();

        List<BoardDto> boardDtoList = new ArrayList<>();
        for (Board board : boards) {
            boardDtoList.add(convertEntityToDto(board));
        }
        return boardDtoList;
    }

    // Board 엔티티를 BoardDto로 변환하는 메서드
    private BoardDto convertEntityToDto(Board board) {
        // Board 엔티티를 BoardDto로 변환하는 로직 구현
        // (예시로는 생성자나 빌더 패턴을 사용하여 변환하도록 하겠습니다.)
        return new BoardDto(board.getTitle(), board.getContent(), board.getWriter());
    }

}
