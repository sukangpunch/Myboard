package com.springboot.board.service;

import com.springboot.board.domain.Board;
import com.springboot.board.domain.User;
import com.springboot.board.dto.BoardDto;
import com.springboot.board.dto.BoardResponseDto;
import com.springboot.board.dto.UserDto;
import com.springboot.board.dto.UserResponseDto;
import com.springboot.board.repository.BoardRepository;
import com.springboot.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BoardRepository boardRepository;


    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없어"));

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User createUser(UserDto userDto) {
        // UserDto를 User 엔티티로 변환하여 데이터베이스에 저장
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
        return userRepository.save(user);
    }

    public User updateUser(Long userId, UserDto userDto) {
        // 데이터베이스에서 userId에 해당하는 User 엔티티를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        // User 엔티티의 필드를 UserInputDto의 값으로 업데이트하고 데이터베이스에 저장
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return userRepository.save(user);
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        } else {
            return false;
        }
    }


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
        return new BoardDto(board.getTitle(), board.getContent(), board.getWriter(),board.getCreatedDate(),board.getModifiedDate());
    }

    private BoardResponseDto convertEntityToResponseDto(Board board) {
        // Board 엔티티를 BoardDto로 변환하는 로직 구현
        // (예시로는 생성자나 빌더 패턴을 사용하여 변환하도록 하겠습니다.)
        return new BoardResponseDto(board.getId(),board.getTitle(), board.getContent(), board.getWriter(),board.getCreatedDate(),board.getModifiedDate());
    }
}
