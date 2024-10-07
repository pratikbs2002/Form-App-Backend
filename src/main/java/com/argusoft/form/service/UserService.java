package com.argusoft.form.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.argusoft.form.entity.User;
import com.argusoft.form.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerNewUser(User user) {
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

    public User getUserConnectionDetails(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
