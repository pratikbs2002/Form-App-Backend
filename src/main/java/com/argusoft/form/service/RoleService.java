package com.argusoft.form.service;

import com.argusoft.form.entity.Role;
import com.argusoft.form.enums.RoleEnum;
import com.argusoft.form.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    public Role findRoleByName(RoleEnum roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }
}
