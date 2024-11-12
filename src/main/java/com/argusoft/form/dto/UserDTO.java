package com.argusoft.form.dto;

import java.util.Date;

public class UserDTO {

    private Long id;
    private String username;
    private String schemaName;
    private RoleDTO role;
    private Date createdAt;
    private boolean deleted;
    private Date deletedAt;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String schemaName, RoleDTO role,
            Date createdAt, boolean deleted, Date deletedAt) {
        this.id = id;
        this.username = username;
        this.schemaName = schemaName;
        this.role = role;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", username=" + username + ", schemaName=" + schemaName + ", role=" + role
                + ", createdAt=" + createdAt + ", deleted=" + deleted + ", deletedAt=" + deletedAt + "]";
    }

}
