package com.example.mourad.navigationandroid;


public class User {

        String id;
        String fullName;
        String email;
        String phone;
        String photoUrl;

    public User(){

    }

    public User(String id, String fullName, String email, String phone,String photoUrl) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.photoUrl = photoUrl;
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


    public  void setPhotoUrl(String photoUrl) {
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



    public  String getPhotoUrl() {
        return photoUrl;
    }
}
