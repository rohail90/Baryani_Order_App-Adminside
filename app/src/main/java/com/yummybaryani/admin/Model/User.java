package com.yummybaryani.admin.Model;

public class User {
    private String name;
    private String password;
    private String phone;
    private String address;
    private String isStaff;

    public User(){}

    public User(String name, String password, String address) {
        this.name = name;
        this.password = password;
        this.address = address;
    }
    public User(String name, String password, String phone, String address, String isStaff) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.isStaff = isStaff;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
