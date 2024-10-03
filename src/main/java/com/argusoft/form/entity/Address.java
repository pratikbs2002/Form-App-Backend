package com.argusoft.form.entity;

public class Address {
    private int id;
    private String add_line;
    private String city;
    private String state;
    private int pincode;

    public Address() {
    }

    public Address(int id, String add_line, String city, String state, int pincode) {
        this.id = id;
        this.add_line = add_line;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdd_line() {
        return add_line;
    }

    public void setAdd_line(String add_line) {
        this.add_line = add_line;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

}
