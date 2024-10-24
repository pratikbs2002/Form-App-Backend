package com.argusoft.form.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
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

    public Page<List<User>> getAllUsersBySchema(String schema, String role,
            org.springframework.data.domain.Pageable pageable) {

        if (role != null && !role.isEmpty() && !role.equals("null")) {
            return userRepository.findBySchemaNameAndRole(schema, role, pageable);
        } else {
            return userRepository.findAllBySchemaName(schema, pageable);
        }
    }
}
