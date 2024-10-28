package com.argusoft.form.repository;

import com.argusoft.form.entity.Role;
import com.argusoft.form.enums.RoleEnum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleEnum roleName);
}
