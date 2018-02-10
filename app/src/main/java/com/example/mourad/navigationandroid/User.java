package com.example.mourad.navigationandroid;


import android.net.Uri;

import java.net.URI;

public class User {

        String id;
        String fullName;
        String email;
        String phone;
        Uri photoUrl;
        String birthday;
        String gender;

    public User(){

    }

    public User(String id, String fullName, String email, String phone,String birthday,String gender,Uri photoUrl) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.photoUrl = photoUrl;
        this.birthday=birthday;
        this.gender=gender;
    }

    public static void setId(String id) {
       id = id;
    }

    public  void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public  void setEmail(String email) {
        this.email = email;
    }

    public  void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public  void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public  String getId() {
        return id;
    }

    public  String getFullName() {
        return fullName;
    }

    public  String getEmail() {
        return email;
    }

    public  String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public  Uri getPhotoUrl() {
        return photoUrl;
    }
}
