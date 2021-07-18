package com.makespace.schedulingsystem.validator;

import com.makespace.schedulingsystem.BookingInfo;
import com.makespace.schedulingsystem.Constants;

public class CapacityValidator extends AbstractValidator {
    @Override
    public boolean validate(BookingInfo bookingInfo) {
        int count = bookingInfo.getAttendeeCount();
        if(count>= Constants.ATTENDEE_LOWER_LIMIT && count <= Constants.ATTENDEE_UPPER_LIMIT){
            return true;
        }
        else {
            setInvalidMessage(Constants.NO_VACANT_ROOM);
            return false;
        }
    }
}
