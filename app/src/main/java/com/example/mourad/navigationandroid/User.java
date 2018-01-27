package com.example.mourad.navigationandroid;


public class User {

        String id;
        String fullNAme;
        String email;
        String phone;
        String password;
        String confPassword;

    public User(){

    }

    public User(String id,String fullNAme, String email, String phone, String password) {
        this.id=id;
        this.fullNAme = fullNAme;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getFullNAme() {
        return fullNAme;
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
    public String getId(){
        return id;
    }
}
