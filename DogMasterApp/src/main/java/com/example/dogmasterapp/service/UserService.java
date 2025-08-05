package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(Integer userId) {
        return userRepository.findUserByUserID(userId);
    }

    public void deleteById(Integer userId) {
        userRepository.deleteById(userId);

    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User patchedUser(Integer userId, User patchedUser) {
        User savedUser = userRepository.findUserByUserID(userId);
        if (patchedUser.getFirstName() != null) {
            savedUser.setFirstName(patchedUser.getFirstName());
        }
        if (patchedUser.getLastName() != null) {
            savedUser.setLastName(patchedUser.getLastName());

        }if (patchedUser.getAddress() != null) {
            savedUser.setAddress(patchedUser.getAddress());
        }if (patchedUser.getPhoneNumber() != null) {
            savedUser.setPhoneNumber(patchedUser.getPhoneNumber());
        }if (patchedUser.getEmail() != null) {
            savedUser.setEmail(patchedUser.getEmail());
        }if (patchedUser.getPassword() != null) {
            savedUser.setPassword(patchedUser.getPassword());
        }
        return userRepository.save(savedUser);
    }
    public User updateUser(Integer userId,User updatedUser){
        User savedUser = userRepository.findUserByUserID(userId);
        savedUser.setFirstName(updatedUser.getFirstName());
        savedUser.setLastName(updatedUser.getLastName());
        savedUser.setAddress(updatedUser.getAddress());
        savedUser.setPhoneNumber(updatedUser.getPhoneNumber());
        savedUser.setEmail(updatedUser.getEmail());
        savedUser.setPassword(updatedUser.getPassword());

        return userRepository.save(savedUser);
    }

}
