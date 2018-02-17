package com.example.mourad.navigationandroid;


public class User {

    private static String id;
    private static String fullName;
    private static String email;
    private static String phone;
    private static String photo;
    private static String birthday;
    private static String gender;


    User(String fullName,String email,String photo){
        User.fullName =fullName;
        User.photo = photo;
        User.email = email;


    }

    User(String id, String fullName, String email, String phone,
         String birthday, String gender, String photo) {
        User.id = id;
        User.fullName = fullName;
        User.email = email;
        User.phone = phone;
        User.photo = photo;
        User.birthday =birthday;
        User.gender =gender;
    }

    User() {

    }

    // Setters

    public void setId(String id) {
        User.id = id;
    }

    public void setFullName(String fullName) {
        User.fullName = fullName;
    }

    public void setEmail(String email) {
        User.email = email;
    }

    public void setBirthday(String birthday) {
        User.birthday = birthday;
    }

    public void setPhone(String phone) {
        User.phone = phone;
    }

    public void setGender(String gender) {
        User.gender = gender;
    }

    public void setPhotoUrl(String photoUrl) {
        photo = photoUrl;
    }

    // Getters

    public  String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public  String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public  String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhotoUrl() {
        return photo;
    }
}
