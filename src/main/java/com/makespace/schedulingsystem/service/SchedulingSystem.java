package com.makespace.schedulingsystem.service;

import com.makespace.schedulingsystem.validator.TimeValidator;
import com.makespace.schedulingsystem.validator.Validator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchedulingSystem {

    private final List<BlockedTime> bufferTimeList;
    private final List<MeetingRoom> meetingRooms;
    private final List<Validator> validators;

    public SchedulingSystem(List<BlockedTime> bufferTimeList, List<MeetingRoom> meetingRooms, List<Validator> validators){
        this.bufferTimeList = bufferTimeList;
        this.meetingRooms = meetingRooms;
        this.validators = validators;
    }
    public  String bookMeetingRoom(String startTime, String endTime, int noOfAttendees) {

        String message = validateInput(startTime, endTime, noOfAttendees);
        if (message.length()>0) {
            return message;
        }
        return checkAndBookMeetingRoom(startTime, endTime, noOfAttendees);
    }


    private String validateInput(String startTime, String endTime, int noOfAttendees) {
        BookingInfo bookingInfo = new BookingInfo(startTime, endTime, noOfAttendees);
        Optional<Validator> result = validators.stream().filter(validator -> !validator.validate(bookingInfo)).findFirst();
        if(result.isPresent()){
            return result.get().getInvalidMessage();
        }

        return "";
    }

    private String checkAndBookMeetingRoom(String startTime, String endTime, int noOfAttendees){
        Optional<MeetingRoom> availableRoom=meetingRooms.stream()
                .sorted(Comparator.comparing(MeetingRoom::getCapacity))
                .filter(meetingRoom -> meetingRoom.isSlotAvailable(startTime, endTime, noOfAttendees, true))
                .findFirst();
        if(availableRoom.isPresent()){
            int bookingStartTimeInt = Integer.parseInt(startTime.replace(":",""));
            int bookingEndTimeInt = Integer.parseInt(endTime.replace(":",""));
            return availableRoom.get().bookSlot(bookingStartTimeInt, bookingEndTimeInt);
        }

        return Constants.NO_VACANT_ROOM;
    }

    public String vacancy(String startTime, String endTime) {
        TimeValidator timeValidator = new TimeValidator(bufferTimeList);
        if(timeValidator.validate( new BookingInfo(startTime, endTime))) {
            String roomNames = getVacantRooms(startTime, endTime, timeValidator);
            if (roomNames != null) return roomNames;
        }
        return timeValidator.getInvalidMessage();
    }

    private String getVacantRooms(String startTime, String endTime, TimeValidator timeValidator) {
        List<MeetingRoom> resultRooms = meetingRooms.stream().filter(
                    meetingRoom -> meetingRoom.isSlotAvailable(startTime, endTime, 0, false)
                )
                .collect(Collectors.toList());
        if (!resultRooms.isEmpty()) {
            StringBuilder roomNames = new StringBuilder();
            resultRooms.forEach(meetingRoom -> roomNames.append(meetingRoom.getName()).append(" "));
            return roomNames.toString().trim();
        }
        else{
            timeValidator.setInvalidMessage(Constants.NO_VACANT_ROOM);
        }
        return null;
    }
}
