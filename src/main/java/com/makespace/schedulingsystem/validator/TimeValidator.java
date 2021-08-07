package com.makespace.schedulingsystem.validator;

import com.makespace.schedulingsystem.service.BlockedTime;
import com.makespace.schedulingsystem.service.BookingInfo;
import com.makespace.schedulingsystem.service.Constants;

import java.util.List;

public class TimeValidator extends AbstractValidator {

    public static final int hrStartIntLimit = 2;
    public static final int maxCharsInTime = 2;
    public static final int hrEndIntLimit1 = 9;
    public static final int hrEndIntLimit2 = 4;
    private final List<BlockedTime> bufferTimeList;

    public TimeValidator(List<BlockedTime> bufferTimeList) {
        this.bufferTimeList = bufferTimeList;
    }

    @Override
    public boolean validate(BookingInfo bookingInfo) {
        if( isValidTime(bookingInfo.getStartTime()) || isValidTime(bookingInfo.getEndTime())){
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
    
    public boolean overlappingBufferTime(String startTime, String endTime) {
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
            return arr.length != 2 || !isValidHourAndTime(arr[0], arr[1]);
        }
        return true;
    }

    private boolean isValidHourAndTime(String hour, String minutes){
        return validHour(hour) && validMinutes(minutes);
    }

        private boolean validMinutes(String minuteVal) {
        if(isNumericTimeData(minuteVal)){
            int minuteStartInt = Character.getNumericValue(minuteVal.charAt(0));
            return minuteStartInt <= Constants.MINUTE_START_DIGIT_LIMIT && validInterval(Integer.parseInt(minuteVal));

        }
        return  false;
    }

    private boolean validInterval(int time){
        return (time % Constants.INTERVAL_LIMIT == 0);
    }

    private boolean validHour(String hourVal) {
        if(isNumericTimeData(hourVal)){
            int hrStartInt = Character.getNumericValue(hourVal.charAt(0));
            int hrEndInt = Character.getNumericValue(hourVal.charAt(1));
            if( hrStartInt <= hrStartIntLimit){
                switch (hrStartInt){
                    case 0 :
                    case 1 :
                        if(hrEndInt <= hrEndIntLimit1){
                            return true;
                        }
                        break;
                    case 2:
                        if(hrEndInt <= hrEndIntLimit2){
                            return true;
                        }
                        break;
                    default:
                        return false;
                }
            }

        }
        return false;
    }

    private boolean isNumericTimeData(String time){
        if(null != time && time.length() == maxCharsInTime){
            char start = time.charAt(0);
            char end = time.charAt(1);
            return Character.isDigit(start) && Character.isDigit(end);
        }
        return false;
    }
}
