package com.example.service.userservice;

import com.example.dto.UserPasswordDto;
import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    public UserResponseDto register(UserRequestDto requestDto);
    public UserResponseDto fetchUser(String email);
    public boolean changerPassword(UserPasswordDto requestDto);
    public List<UserResponseDto> fetchAllUser();
}
