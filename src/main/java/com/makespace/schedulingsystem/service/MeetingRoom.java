package com.makespace.schedulingsystem.service;

import java.util.ArrayList;
import java.util.List;

public class MeetingRoom {
    private final String name;
    private final int capacity;
    private final List<BlockedTime> bookedTimes;

    public MeetingRoom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.bookedTimes = new ArrayList<>();
    }

    public boolean isSlotAvailable(String startTime, String endTime, int reqdCapacity, boolean doCapacityCheck){
       if(doCapacityCheck && reqdCapacity>capacity){
           return false;
       }

        return bookedTimes.stream().noneMatch(blockedTime ->
                blockedTime.overlapsWithBookingTime(startTime, endTime));

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
