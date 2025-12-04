package com.patinaje.v1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patinaje.v1.repository.userRepository;

import com.patinaje.v1.model.userModel;

@Service
public class userService {
    
    @Autowired
    userRepository userRepository;

    public List<userModel> getAllUsers() {
        return userRepository.findAll();
    }


}
