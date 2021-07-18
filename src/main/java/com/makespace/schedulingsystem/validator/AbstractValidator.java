package com.makespace.schedulingsystem.validator;

public abstract class AbstractValidator implements Validator {
    String invalidMessage = "";
    @Override
    public String getInvalidMessage() {
        return invalidMessage;
    }

    public void setInvalidMessage(String message){
        this.invalidMessage = message;
    }
}
