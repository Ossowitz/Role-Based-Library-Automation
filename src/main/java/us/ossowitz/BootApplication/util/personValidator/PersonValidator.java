package us.ossowitz.BootApplication.util.personValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import us.ossowitz.BootApplication.models.Person;
import us.ossowitz.BootApplication.services.PeopleService;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        validateEmail(person, errors);
        validatePhoneNumber(person, errors);
    }

    private void validatePhoneNumber(Person person, Errors errors) {
        if (peopleService.isPhoneNumberExists(person.getPhoneNumber())) {
            errors.rejectValue("phoneNumber", "", "This phone number already will be taken");
        }
    }

    private void validateEmail(Person person, Errors errors) {
        if (peopleService.isEmailExists(person.getEmail())) {
            errors.rejectValue("email", "", "This email already will be taken");
        }
    }
}
