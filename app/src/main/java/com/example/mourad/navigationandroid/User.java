package com.example.mourad.navigationandroid;


public class User {

    private static String id;
    private static String fullName;
    private static String email;
    private static String phone;
    private static String photo;
    private static String birthday;
    private static String gender;

    User(){

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

    // Setters

    public static void setId(String id) {
        User.id = id;
    }

    public static void setFullName(String fullName) {
        User.fullName = fullName;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public void setBirthday(String birthday) {
        User.birthday = birthday;
    }

    public static void setPhone(String phone) {
        User.phone = phone;
    }

    public static void setGender(String gender) {
        User.gender = gender;
    }

    public static void setPhotoUrl(String photoUrl) {
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
