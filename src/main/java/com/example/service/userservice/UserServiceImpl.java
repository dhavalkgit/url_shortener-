package com.example.service.userservice;

import com.example.dto.UserPasswordDto;
import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;
import com.example.exception.ResourceNotFound;
import com.example.exception.UserAlreadyException;
import com.example.model.User;
import com.example.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRequestDto requestDto) {
        Optional<User> oldUser = userRepo.findByEmail(requestDto.getEmail());
        if(oldUser.isPresent()) throw  new UserAlreadyException("user already present with this email id");

        User user = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        User savedUser = userRepo.save(user);

        return UserResponseDto.builder()
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .id(savedUser.getId())
                .build();
    }

    @Override
    public UserResponseDto fetchUser(String email) {
        Optional<User> byEmail = userRepo.findByEmail(email);
        if(byEmail.isPresent()){
            User user = byEmail.get();
            return UserResponseDto.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .id(user.getId())
                    .build();
        }
        throw new ResourceNotFound("user not present with this user id");
    }

    @Override
    public boolean changerPassword(UserPasswordDto requestDto) {
        Optional<User> user = userRepo.findByEmail(requestDto.getEmail());
        if(user.isPresent()){
            String oldPassword = passwordEncoder.encode(requestDto.getOldPassword());
            String newPassword = passwordEncoder.encode(requestDto.getNewPassword());

            if(user.get().getPassword().equals(oldPassword)){
                userRepo.changePassword(requestDto.getEmail(), newPassword);
                return true;
            }
            else{
                throw new RuntimeException("please enter correct old password");
            }
        }
        else{
            throw new ResourceNotFound("user with this username is not present");
        }
    }

    @Override
    public List<UserResponseDto> fetchAllUser() {
        List<User> allUsers = userRepo.findAll();
        return allUsers.stream().map((user)-> UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build()).toList();
    }

}
