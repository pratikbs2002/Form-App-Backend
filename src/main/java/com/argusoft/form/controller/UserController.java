package com.argusoft.form.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.argusoft.form.dto.RoleDTO;
import com.argusoft.form.dto.UserDTO;
import com.argusoft.form.entity.Role;
import com.argusoft.form.entity.User;
import com.argusoft.form.enums.RoleEnum;
import com.argusoft.form.security.datasource_config.UserContextHolder;
import com.argusoft.form.service.DbUserRegistrationService;
import com.argusoft.form.service.RoleService;
import com.argusoft.form.service.SchemaMappingService;
import com.argusoft.form.service.UserService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private RoleService roleService;
    private final UserService userService;

    private final DbUserRegistrationService dbUserRegistrationService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    public UserController(UserService userService, DbUserRegistrationService dbUserRegistrationService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.dbUserRegistrationService = dbUserRegistrationService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{username}")
    public User getUserDetails(@PathVariable String username) {
        return userService.getUserConnectionDetails(username);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all/{schemaName}")
    public ResponseEntity<Page<UserDTO>> getAllUserBySchemaName(
            @PathVariable String schemaName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role) {
        if (schemaName == null || schemaName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Role roleObj = null;
            if (role != null && !role.trim().equalsIgnoreCase("all")) {
                try {
                    RoleEnum roleEnum = RoleEnum.valueOf(role.toUpperCase());
                    roleObj = roleService.findRoleByName(roleEnum);
                } catch (IllegalArgumentException e) {
                    System.out.println("No matching RoleEnum for: " + role);
                    return ResponseEntity.badRequest().body(null);
                }
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<User> usersPage;

            if (roleObj != null && roleObj.getRoleId() != null) {
                usersPage = userService.getAllUsersBySchema(schemaName, roleObj, pageable);
            } else {
                usersPage = userService.getAllUsersBySchema(schemaName, pageable);
            }

            if (usersPage.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            Page<UserDTO> userDTOsPage = usersPage.map(user -> {
                RoleDTO roleDTO = new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName());
                return new UserDTO(user.getId(), user.getUsername(), user.getSchemaName(), roleDTO,
                        user.getCreated_at(), user.isDeleted(), user.getDeleted_at());
            });

            return ResponseEntity.ok(userDTOsPage);

        } catch (DataAccessException e) {
            System.out.println("Database access error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all/root")
    public ResponseEntity<Page<UserDTO>> getAllUsersForRoot(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Role roleObj = new Role();

            if (role != null && !role.trim().equalsIgnoreCase("all")) {
                try {
                    RoleEnum roleEnum = RoleEnum.valueOf(role.toUpperCase());
                    roleObj = roleService.findRoleByName(roleEnum);
                } catch (IllegalArgumentException e) {
                    System.out.println("No matching RoleEnum for: " + role);
                }
            }

            Page<User> usersPage;
            if (roleObj != null && roleObj.getRoleId() != null) {
                usersPage = userService.getAllUsersForRoot(roleObj, pageable);
            } else {
                usersPage = userService.getAllUsersForRoot(pageable);
            }

            if (usersPage.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            Page<UserDTO> userDTOsPage = usersPage.map(user -> {
                RoleDTO roleDTO = new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName());
                return new UserDTO(user.getId(), user.getUsername(), user.getSchemaName(), roleDTO,
                        user.getCreated_at(), user.isDeleted(), user.getDeleted_at());
            });

            return ResponseEntity.ok(userDTOsPage);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/add/user")
    public ResponseEntity<?> addAdminUser(@RequestBody Map<String, String> userData) {
        String schemaUUID;
        try (Connection connection = dataSource.getConnection()) {
            schemaUUID = connection.getSchema();
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        User user = new User();
        user.setSchemaName(schemaUUID);
        user.setUsername(userData.get("username") + "_" + UserContextHolder.getSchema() + "_" + userData.get("role"));
        user.setPassword(passwordEncoder.encode(userData.get("password")));
        System.out.println(userData.get("role"));
        try {
            if (userData.get("role").toUpperCase().equals("ADMIN")) {
                Role role = roleService.findRoleByName(RoleEnum.ADMIN);
                user.setRole(role);
            } else if (userData.get("role").toUpperCase().equals("REPORTING_USER")) {
                Role role = roleService.findRoleByName(RoleEnum.REPORTING_USER);
                System.out.println("gfgg");
                System.out.println(role);
                user.setRole(role);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Somthing wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Add Database user details in public schema user table
        try {
            userService.registerNewUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        RoleDTO roleDTO = new RoleDTO(user.getRole().getRoleId(), user.getRole().getRoleName());
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getSchemaName(), roleDTO,
                user.getCreated_at(), user.isDeleted(), user.getDeleted_at());

        return ResponseEntity.ok(userDTO);
    }

    // Soft delete a user by ID
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<String> softDeleteUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.softDeleteUser(id);
            return new ResponseEntity<>("User soft deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // Restore a soft deleted user
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent() && user.get().isDeleted()) {
            userService.restoreUser(id);
            return new ResponseEntity<>("User restored successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found or not deleted", HttpStatus.NOT_FOUND);
        }
    }
}
