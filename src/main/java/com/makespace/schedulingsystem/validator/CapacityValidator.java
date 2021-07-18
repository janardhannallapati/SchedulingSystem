package com.makespace.schedulingsystem.validator;

import com.makespace.schedulingsystem.BookingInfo;

public class CapacityValidator extends AbstractValidator {
    @Override
    public boolean validate(BookingInfo bookingInfo) {
        return true;
    }
}
