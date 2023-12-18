package ru.alishev.springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alishev.springcourse.dao.PersonDAO;
import ru.alishev.springcourse.models.Person;

//самописный валидатор
@Component
public class PersonValidator implements Validator {

    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
//        return false;
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        //посмотреть, есть ли человек с таким же email'ом
//        if( personDAO.show(person.getEmail()) != null);
        if( personDAO.show(person.getEmail()).isPresent()) //если такой существует - кладем ошибку
            errors.rejectValue("email", "", "This email is already taken"); //второй аргумент - код ошибки, пока не юзаем
//        personDAO.show()
    }
}
