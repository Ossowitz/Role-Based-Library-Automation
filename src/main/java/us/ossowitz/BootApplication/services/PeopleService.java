package us.ossowitz.BootApplication.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.ossowitz.BootApplication.models.Book;
import us.ossowitz.BootApplication.models.Person;
import us.ossowitz.BootApplication.repositories.PeopleRepository;
import us.ossowitz.BootApplication.security.PersonWrapper;
import us.ossowitz.BootApplication.util.exception.NotFoundException;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    private static final String NOT_REGISTERED = "This user is not registered";

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByName(String name) {
        return peopleRepository.findByName(name);
    }

    public boolean isEmailExists(String email) {
        return peopleRepository.existsByEmail(email);
    }

    public boolean isPhoneNumberExists(String phoneNumber) {
        return peopleRepository.existsByPhoneNumber(phoneNumber);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            // Book expiration check
            person.get().getBooks().forEach(book -> {
                long diffInMillis = Math.abs(book.getTakenAt().getTime() - new Date().getTime());

                if (diffInMillis > 864_000_000) {
                    book.setExpired(true); // Overdue book
                }
            });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    public Person checkByPersonName(String name) {
        return peopleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(NOT_REGISTERED));
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return new PersonWrapper(checkByPersonName(name));
    }
}
