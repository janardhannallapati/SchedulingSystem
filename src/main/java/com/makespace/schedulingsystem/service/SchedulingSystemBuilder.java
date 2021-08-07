package com.makespace.schedulingsystem.service;

import com.makespace.schedulingsystem.validator.CapacityValidator;
import com.makespace.schedulingsystem.validator.TimeValidator;
import com.makespace.schedulingsystem.validator.Validator;

import java.util.ArrayList;
import java.util.List;

public class SchedulingSystemBuilder {



 public SchedulingSystem build(){
    List<MeetingRoom> meetingRooms = createMeetingRooms();
    List<BlockedTime> bufferTimes = createBufferTimes();
    List<Validator> validators = createValidators(bufferTimes);
    return new SchedulingSystem(bufferTimes, meetingRooms, validators);
 }
    private List<MeetingRoom> createMeetingRooms() {
        List<MeetingRoom> meetingRooms = new ArrayList<>();
        meetingRooms.add(new MeetingRoom("C-Cave", Constants.CCAPACITY));
        meetingRooms.add(new MeetingRoom("D-Tower",Constants.DCAPACITY));
        meetingRooms.add(new MeetingRoom("G-Mansion",Constants.GCAPACITY));
        return meetingRooms;
    }

    private List<BlockedTime> createBufferTimes() {
        List<BlockedTime> bufferTimes = new ArrayList<>();
        bufferTimes.add(new BlockedTime("09:00","09:15"));
        bufferTimes.add(new BlockedTime("13:15","13:45"));
        bufferTimes.add(new BlockedTime("18:45","19:00"));
        return bufferTimes;
    }

    private static List<Validator> createValidators(List<BlockedTime> bufferTimes){
        List<Validator> validators = new ArrayList<>();
        TimeValidator timeValidator = new TimeValidator(bufferTimes);
        validators.add(timeValidator);
        validators.add(new CapacityValidator());
        return validators;
    }


}
