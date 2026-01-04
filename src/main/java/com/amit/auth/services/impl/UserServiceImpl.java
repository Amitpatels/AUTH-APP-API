package com.amit.auth.services.impl;

import com.amit.auth.dtos.UserDto;
import com.amit.auth.entites.User;
import com.amit.auth.enums.Provider;
import com.amit.auth.exceptions.ResourceNotFoundException;
import com.amit.auth.repositories.UserRepository;
import com.amit.auth.services.UserService;
import com.amit.auth.utility.UserHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        if(userDto.getEmail() == null || userDto.getEmail().isBlank()){
            throw new IllegalArgumentException("Email is required !!");
        }

        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exisit !!");
        }

        //If users have extra check put here

        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);

        //TODO: role assign here for user authorization

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uid = UserHelper.parseUUID(userId);
        User existingUser = userRepository
                .findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));

        //we are not updating the email id for these project
        if(userDto.getName() != null) existingUser.setName(userDto.getName());
        if(userDto.getImage() != null) existingUser.setImage(userDto.getImage());
        if(userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
        //TODO: set the password updation logic...
        if(userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnable(userDto.isEnable());

        User updateUser = userRepository.save(existingUser);
        return modelMapper.map(updateUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
            User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given email id !!"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uid = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uid).orElseThrow(()-> new ResourceNotFoundException("User not found with given id !!"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(UserHelper.parseUUID(userId)).orElseThrow(()-> new ResourceNotFoundException("User not found with given id !!"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class)).toList();
    }
}
