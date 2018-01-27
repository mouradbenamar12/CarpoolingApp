package com.example.mourad.navigationandroid;


public class User {

        String id;
        String fullName;
        String email;
        String phone;
        String password;
        String confPassword;

    public User(){

    }

    public User(String id, String fullName, String email, String phone, String password) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}
