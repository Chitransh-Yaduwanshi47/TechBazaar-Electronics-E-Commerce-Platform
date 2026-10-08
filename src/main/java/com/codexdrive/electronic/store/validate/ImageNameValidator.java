//package com.codexdrive.electronic.store.validate;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {
//    private Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);
//
//    @Override
//    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
//
//        logger.info("Message from isValid : {} ", value);
//        //logic
//        if (value.isBlank()) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//}

package com.codexdrive.electronic.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

    private Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        logger.info("Message from isValid : {}", s); // s = value 

        // logic
        if (s == null || s.isBlank()) {
            return false;
        } else {
            return true;
        }
    }
}