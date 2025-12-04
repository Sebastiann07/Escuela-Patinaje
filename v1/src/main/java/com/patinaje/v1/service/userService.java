package com.patinaje.v1.service;

import java.util.List;
import java.util.Optional;

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

    public Optional<userModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public userModel createUser(userModel user) {
        return userRepository.save(user);
    }


    public userModel updateUser(Long id, userModel userDetails) {
        Optional<userModel> user = userRepository.findById(id);
        
        if (user.isPresent()) {
            userModel existingUser = user.get();
            
            if (userDetails.getName() != null) {
                existingUser.setName(userDetails.getName());
            }
            if (userDetails.getEmail() != null) {
                existingUser.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPassword() != null) {
                existingUser.setPassword(userDetails.getPassword());
            }
            if (userDetails.getPhone() != null) {
                existingUser.setPhone(userDetails.getPhone());
            }
            
            return userRepository.save(existingUser);
        }
        
        return null;
    }


    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public Optional<userModel> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}