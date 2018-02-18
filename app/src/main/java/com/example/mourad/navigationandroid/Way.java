package com.example.mourad.navigationandroid;

/**
 * Created by abdelle on 2/17/18.
 */

public class Way {

    private String Source;
    private String Destination;
    private String CarId;
    private String date;
    private String time;

    public Way(String source, String destination, String carId, String date, String time) {
        Source = source;
        Destination = destination;
        CarId = carId;
        this.date = date;
        this.time = time;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getCarId() {
        return CarId;
    }

    public void setCarId(String carId) {
        CarId = carId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
