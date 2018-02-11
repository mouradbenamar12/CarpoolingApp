package com.example.mourad.navigationandroid;


public class User {

        static String id;
        static String fullName;
        static String email;
        static String phone;
        static String photo;
        static String birthday;
        static String gender;

    public User(){

    }

    public User(String id, String fullName, String email, String phone,String birthday,String gender,String photo) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
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

    public  void setPhotoUrl(String photoUrl) {
        this.photo = photoUrl;
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

    public  String getPhotoUrl() {
        return photo;
    }
}
