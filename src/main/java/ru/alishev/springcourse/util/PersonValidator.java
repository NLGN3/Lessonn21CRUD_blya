package ru.alishev.springcourse.util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PersonValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
