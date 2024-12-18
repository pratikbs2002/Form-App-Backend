package com.argusoft.form.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.argusoft.form.dto.RoleDTO;
import com.argusoft.form.dto.UserDTO;
import com.argusoft.form.entity.Role;
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

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getSchemaName(),
                        new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName()), user.getCreated_at(),
                        user.isDeleted(), user.getDeleted_at()))
                .collect(Collectors.toList());
    }

    public Page<User> getAllUsersBySchema(String schema, Role role, Pageable pageable) {
        return userRepository.findBySchemaNameAndRole(schema, role, pageable);
    }

    public Page<User> getAllUsersBySchema(String schema, Pageable pageable) {
        return userRepository.findAllBySchemaName(schema, pageable);
    }

    public Page<User> getAllUsersForRoot(Pageable pageable) {
        return userRepository.findAllUsersForRoot(pageable);
    }

    public Page<User> getAllUsersForRoot(Role role, Pageable pageable) {
        return userRepository.findAllUsersForRootByRole(role, pageable);
    }

    public void softDeleteUser(Long id) {
        userRepository.softDeleteById(id);
    }

    public void restoreUser(Long id) {
        userRepository.restoreById(id);
    }

}
