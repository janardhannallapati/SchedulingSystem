package com.makespace.schedulingsystem;

import com.makespace.schedulingsystem.validator.TimeValidator;
import com.makespace.schedulingsystem.validator.Validator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SchedulingSystem {

    private List<BlockedTime> bufferTimeList;
    private List<MeetingRoom> meetingRooms;
    private List<Validator> validators;

    public SchedulingSystem(){
        initializeBufferTimes();
        initializeMeetingRooms();
        initializeValidators();
    }

    private void initializeMeetingRooms() {
        meetingRooms = new ArrayList<>();
        meetingRooms.add(new MeetingRoom("C-Cave", Constants.CCAPACITY));
        meetingRooms.add(new MeetingRoom("D-Tower",Constants.DCAPACITY));
        meetingRooms.add(new MeetingRoom("G-Mansion",Constants.GCAPACITY));
    }

    private void initializeBufferTimes() {
        bufferTimeList = new ArrayList<>();
        bufferTimeList.add(new BlockedTime("09:00","09:15"));
        bufferTimeList.add(new BlockedTime("13:15","13:45"));
        bufferTimeList.add(new BlockedTime("18:45","19:00"));
    }

    private void initializeValidators(){
        validators = new ArrayList<>();
        TimeValidator timeValidator = new TimeValidator(bufferTimeList);
        validators.add(timeValidator);
    }

    public  String bookMeetingRoom(String action, String startTime, String endTime, int noOfAttendees) {

        String message = validateInput(startTime, endTime, noOfAttendees);
        if (message != null) return message;
        return checkAndBookMeetingRoom(startTime, endTime, noOfAttendees);
    }

    private String validateInput(String startTime, String endTime, int noOfAttendees) {
        BookingInfo bookingInfo = new BookingInfo(startTime, endTime, noOfAttendees);
        Optional<Validator> result = validators.stream().filter(validator -> !validator.validate(bookingInfo)).findFirst();
        if(result.isPresent()){
            return result.get().getInvalidMessage();
        }

        if(!validCapacity(noOfAttendees)){
            return Constants.NO_VACANT_ROOM;
        }

        return null;
    }

    private String checkAndBookMeetingRoom(String startTime, String endTime, int noOfAttendees){
        Optional<MeetingRoom> availableRoom=meetingRooms.stream()
                .sorted(Comparator.comparing(MeetingRoom::getCapacity))
                .filter(meetingRoom -> meetingRoom.isSlotAvailable(startTime, endTime, noOfAttendees))
                .findFirst();
        if(availableRoom.isPresent()){
            int bookingStartTimeInt = Integer.parseInt(startTime.replace(":",""));
            int bookingEndTimeInt = Integer.parseInt(endTime.replace(":",""));
            return availableRoom.get().bookSlot(bookingStartTimeInt, bookingEndTimeInt);
        }

        return Constants.NO_VACANT_ROOM;
    }

    private boolean validCapacity(int noOfAttendees){
        if(noOfAttendees>= Constants.ATTENDEE_LOWER_LIMIT && noOfAttendees <= Constants.ATTENDEE_UPPER_LIMIT){
            return true;
        }
        return false;
    }



}
