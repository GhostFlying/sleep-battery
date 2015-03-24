package com.ghostflying.autobatterysaver.model;

/**
 * Created by ghostflying on 3/24/15.
 */
public class Time {
    int hour;
    int minute;

    public Time(int hour, int minute){
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour(){
        return hour;
    }

    public int getMinute(){
        return minute;
    }
}
