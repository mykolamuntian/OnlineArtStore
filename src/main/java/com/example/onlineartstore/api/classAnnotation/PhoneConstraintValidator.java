package com.example.onlineartstore.api.classAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneConstraintValidator implements ConstraintValidator<Phone, String> {
    @Override
    public void initialize(Phone phone) { }

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext cxt) {
        if(phoneField == null) {
            return false;
        }
        return phoneField.matches("\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{2}\\2([0-9]{2}))");
    }

    //(123) 456 78 99
    //(123).456.78.99
    //(123)-456-78-99
    //123-456-78-99
    //123 456 78 99
    //1234567899
    //are supported
}
