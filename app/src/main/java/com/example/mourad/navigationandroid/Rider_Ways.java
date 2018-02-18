package com.example.mourad.navigationandroid;


public class Rider_Ways {

    private int image_ways;
    private String Full_Name;
    private String Source;
    private String Destination;
    private String Date;
    private String Time;
    private double Rating;


    public Rider_Ways(int image_ways, String full_Name, String source,
                       String destination, String date, String time, double Rating)
    {
        this.image_ways = image_ways;
        Full_Name = full_Name;
        Source = source;
        Destination = destination;
        Date = date;
        Time = time;
        this.Rating=Rating;
    }

    public int getImage_ways() {
        return image_ways;
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

    public double getRating() {
        return Rating;
    }
}
