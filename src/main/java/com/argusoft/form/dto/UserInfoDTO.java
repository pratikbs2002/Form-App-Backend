package com.argusoft.form.dto;

import com.argusoft.form.entity.Address;

public class UserInfoDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private RoleDTO role;
    private Address address;
    private Long location;

    public UserInfoDTO() {
    }

    public UserInfoDTO(Long id, String firstName, String lastName, RoleDTO role, Address address, Long location) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.address = address;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public RoleDTO getRoleDTO() {
        return role;
    }

    public void setRoleDTO(RoleDTO role) {
        this.role = role;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }
}
