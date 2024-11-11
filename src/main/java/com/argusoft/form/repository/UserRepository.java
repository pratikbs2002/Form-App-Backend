package com.argusoft.form.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.argusoft.form.entity.Role;
import com.argusoft.form.entity.User;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);

        List<User> findBySchemaName(String schemaName);

        Page<User> findAllBySchemaName(String schemaName,
                        Pageable pageable);

        Page<User> findBySchemaNameAndRole(String schemaName, Role role,
                        Pageable pageable);

        @Query("SELECT u FROM User u")
        Page<User> findAllUsersForRoot(Pageable pageable);

        @Query("SELECT u FROM User u WHERE u.role = :role")
        Page<User> findAllUsersForRootByRole(Role role, Pageable pageable);
}
