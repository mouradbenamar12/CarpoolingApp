package com.example.mourad.navigationandroid;


public class Rider_Ways {

    private String Image_ways;
    private String Full_Name;
    private String Source;
    private String Destination;
    private String Date;
    private String Time;
    private String Phone;
    private String CarId;
    private String LatLngSrc;
    private String LatLngDes;


    public Rider_Ways() {
    }

    public Rider_Ways(String image_ways, String full_Name, String source, String destination,
                      String date, String time, String phone, String carId,String LatLngSrc,String LatLngDes)
    {
        this.Image_ways = image_ways;
        this.Full_Name = full_Name;
        this.Source = source;
        this.Destination = destination;
        this.Date = date;
        this.Time = time;
        this.Phone=phone;
        this.CarId=carId;
        this.LatLngSrc=LatLngSrc;
        this.LatLngDes=LatLngDes;

    }

    public String getLatLngSrc() {
        return LatLngSrc;
    }

    public void setLatLngSrc(String latLngSrc) {
        LatLngSrc = latLngSrc;
    }

    public String getLatLngDes() {
        return LatLngDes;
    }

    public void setLatLngDes(String latLngDes) {
        LatLngDes = latLngDes;
    }
// Getters

    public String getImage_ways() {
        return Image_ways;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public String getSource() {
        return Source;
    }

    public String getDestination() {
        return Destination;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getPhone() {
        return Phone;
    }

    public String getCarId() {
        return CarId;
    }


    // Setters


    public void setImage_ways(String image_ways) {
        Image_ways = image_ways;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public void setSource(String source) {
        Source = source;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setCarId(String carId) {
        CarId = carId;
    }
}
