package com.makespace.schedulingsystem.validator;

import com.makespace.schedulingsystem.BookingInfo;

public interface Validator {
    String getInvalidMessage();
    boolean validate(BookingInfo bookingInfo);
}
