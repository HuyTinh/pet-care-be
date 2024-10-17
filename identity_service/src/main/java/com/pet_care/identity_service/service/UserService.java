package com.pet_care.identity_service.service;

import com.pet_care.identity_service.dto.request.UserCreationRequest;
import com.pet_care.identity_service.dto.request.UserUpdateRequest;
import com.pet_care.identity_service.entity.User;
import com.pet_care.identity_service.exception.ErrorCode;
import com.pet_care.identity_service.exception.IdentityException;
import com.pet_care.identity_service.mapper.UserMapper;
import com.pet_care.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User createRequest(UserCreationRequest request) {
        if(userRepository.existsByEmail(request.getEmail()))
            throw new IdentityException(ErrorCode.USER_EXISTED);
        return userRepository.save(userMapper.toEntity(request));
    }

    public User updateRequest(Long id,UserUpdateRequest request) {
        User existUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
        return userRepository.save(userMapper.partialUpdate(request,existUser));
    }

    public void deleteRequest(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
