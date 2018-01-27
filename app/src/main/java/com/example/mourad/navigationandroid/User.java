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

    public User(String id,String fullNAme, String email, String phone, String password, String confPassword) {
        this.id=id;
        this.fullNAme = fullNAme;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.confPassword = confPassword;
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
    public String getConfPassword() {
        return confPassword;
    }
    public String getId(){
        return id;
    }
}
