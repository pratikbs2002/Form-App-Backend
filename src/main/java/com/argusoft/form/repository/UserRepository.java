package com.argusoft.form.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.argusoft.form.entity.User;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findBySchemaName(String schemaName);

    Page<List<User>> findAllBySchemaName(String schemaName,
            Pageable pageable);

    Page<List<User>> findBySchemaNameAndRole(String schemaName, String role,
            Pageable pageable);
}
