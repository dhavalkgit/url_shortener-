package com.example.controller;

import com.example.dto.UserLoginDto;
import com.example.dto.UserPasswordDto;
import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;
import com.example.security.JwtService;
import com.example.service.userservice.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto){
        UserResponseDto responseDto = userService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDto.getUserName(), userLoginDto.getPassword()));

        if(authenticate.isAuthenticated()) {
            String jwtToken = jwtService.createToken(userLoginDto.getUserName(), "User");
            return new ResponseEntity<>(Map.of("JwtToken",jwtToken), HttpStatus.OK);
        }
        return new ResponseEntity<>("Authentication is failed", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String userId){
        UserResponseDto responseDto = userService.fetchUser(userId);
        return ResponseEntity.status(HttpStatus.FOUND).body(responseDto);
    }

//    @GetMapping("/")
//    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
//        return ResponseEntity.status(HttpStatus.OK).body(userService.fetchAllUser());
//    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody UserPasswordDto requestDto){
        boolean res = userService.changerPassword(requestDto);
        if(res){
            return new ResponseEntity<>(Map.of("Message","Success"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("Message","Check you old password"), HttpStatus.BAD_REQUEST);
    }
}
