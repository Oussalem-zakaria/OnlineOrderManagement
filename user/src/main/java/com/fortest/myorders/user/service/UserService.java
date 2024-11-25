package com.fortest.myorders.user.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.fortest.myorders.user.bean.User;
import com.fortest.myorders.user.repository.UserRepository;
import com.fortest.myorders.user.request.UserRequest;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    public User createUser(UserRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();

        return userRepository.saveAndFlush(user);
        // todo: check if email valid
        // todo: check if email not taken

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public User updateUser(Integer id, UserRequest request) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            return userRepository.save(user);
        }).orElseGet(() -> {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .build();
            return userRepository.save(user);
        });
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
