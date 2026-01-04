package com.amit.auth.services.impl;

import com.amit.auth.dtos.UserDto;
import com.amit.auth.services.AuthService;
import com.amit.auth.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {
        //logic
        //verify email
        //verify password
        //default roles

        return userService.createUser(userDto);
    }
}