package com.makespace.schedulingsystem.service;

public class BookingInfo{

    private String startTime;
    private String endTime;
    private int attendeeCount;

    public BookingInfo(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public BookingInfo(String startTime, String endTime, int attendeeCount) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.attendeeCount = attendeeCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }
}

