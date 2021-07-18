package com.makespace.schedulingsystem.validator;

import com.makespace.schedulingsystem.BlockedTime;
import com.makespace.schedulingsystem.BookingInfo;
import com.makespace.schedulingsystem.Constants;

import java.util.List;

public class TimeValidator extends AbstractValidator {

    private final List<BlockedTime> bufferTimeList;

    public TimeValidator(List<BlockedTime> bufferTimeList) {
        this.bufferTimeList = bufferTimeList;
    }

    @Override
    public boolean validate(BookingInfo bookingInfo) {
        if( !isValidTime(bookingInfo.getStartTime()) || !isValidTime(bookingInfo.getEndTime()) ){
            setInvalidMessage(Constants.INCORRECT_INPUT);
            return false;
        }

        int startTimeInt = Integer.parseInt(bookingInfo.getStartTime().replace(":",""));
        int endTimeInt = Integer.parseInt(bookingInfo.getEndTime().replace(":",""));
        if(startTimeInt >= endTimeInt){
            setInvalidMessage(Constants.INCORRECT_INPUT);
            return false;
        }

        if(overlappingBufferTime(bookingInfo.getStartTime(), bookingInfo.getEndTime())){
            setInvalidMessage(Constants.NO_VACANT_ROOM);
            return false;
        }     
        
        return true;
    }
    
    private boolean overlappingBufferTime(String startTime, String endTime) {
        for(BlockedTime bufferTime: bufferTimeList){
            if(bufferTime.overlapsWithBookingTime(startTime, endTime)){
                return true;
            }
        }
        return false;
    }

    private boolean isValidTime(String time) {
        if(null != time && time.length()>0){
            String[] arr=time.split(":");
            if(null != arr && arr.length == 2){
                if(validHour(arr[0]) && validMinutes(arr[1])){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validMinutes(String minuteVal) {
        if(isNumericTimeData(minuteVal)){
            int minuteStartInt = Character.getNumericValue(minuteVal.charAt(0));
            int minuteEndInt = Character.getNumericValue(minuteVal.charAt(1));
            if(minuteStartInt < 6 && validInterval(Integer.parseInt(minuteVal))){
                return true;
            }

        }
        return  false;
    }

    private boolean validInterval(int time){
        if(time == 0 || time%Constants.INTERVAL_LIMIT == 0 ){
            return true;
        }

        return false;
    }

    private boolean validHour(String hourVal) {
        if(isNumericTimeData(hourVal)){
            int hrStartInt = Character.getNumericValue(hourVal.charAt(0));
            int hrEndInt = Character.getNumericValue(hourVal.charAt(1));
            if( hrStartInt <= 2){
                switch (hrStartInt){
                    case 0 :
                    case 1 :
                        if(hrStartInt >= 0 && hrEndInt <= 9){
                            return true;
                        }
                        break;
                    case 2:
                        if(hrEndInt < 5){
                            return true;
                        }
                    default:
                        return false;
                }
            }

        }
        return false;
    }

    private boolean isNumericTimeData(String time){
        if(null != time && time.length() == 2){
            char start = time.charAt(0);
            char end = time.charAt(1);
            if(Character.isDigit(start) && Character.isDigit(end)) {
                return true;
            }
        }
        return false;
    }
}
