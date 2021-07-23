package com.makespace.schedulingsystem.service;

import java.util.ArrayList;
import java.util.List;

public class MeetingRoom {
    private String name;
    private int capacity;
    private List<BlockedTime> bookedTimes;

    public MeetingRoom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.bookedTimes = new ArrayList<>();
    }

    public boolean isSlotAvailable(String startTime, String endTime, int reqdCapacity, boolean doCapacityCheck){
       if(doCapacityCheck && reqdCapacity>capacity){
           return false;
       }

        return bookedTimes.stream().filter(
                 blockedTime ->
                        blockedTime.overlapsWithBookingTime(startTime, endTime)
        )
                .count() == 0;

    }

    /*
      Must call only after isSlotAvailable() method returns true.
     */
    public String bookSlot(int startTime, int endTime){
        bookedTimes.add(new BlockedTime(startTime, endTime));
        return this.name;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<BlockedTime> getBookedTimes() {
        return bookedTimes;
    }
}
