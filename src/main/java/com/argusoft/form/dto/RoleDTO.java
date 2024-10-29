package com.argusoft.form.dto;

import com.argusoft.form.enums.RoleEnum;

public class RoleDTO {
    private Long roleId;
    private RoleEnum roleType;

    public RoleDTO() {
    }

    public RoleDTO(Long roleId, RoleEnum roleType) {
        this.roleId = roleId;
        this.roleType = roleType;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public RoleEnum getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleEnum roleType) {
        this.roleType = roleType;
    }
}
