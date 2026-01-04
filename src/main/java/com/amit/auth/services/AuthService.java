package com.amit.auth.services;

import com.amit.auth.dtos.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);

}
