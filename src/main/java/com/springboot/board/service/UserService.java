package com.springboot.board.service;

import com.springboot.board.domain.User;
import com.springboot.board.dto.UserDto;
import com.springboot.board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElse(null);
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
}
