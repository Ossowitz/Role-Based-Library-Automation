package us.ossowitz.BootApplication.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import us.ossowitz.BootApplication.models.Person;
import us.ossowitz.BootApplication.services.PeopleService;
import us.ossowitz.BootApplication.util.personValidator.PersonValidator;

import java.util.List;

@RestController
@RequestMapping("/api/v1/people")
@AllArgsConstructor
public class PeopleController {

    private final PeopleService peopleService;

    private final PersonValidator personValidator;

    @GetMapping
    public ResponseEntity<List<Person>> getPeople() {
        List<Person> people = peopleService.findAll();
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") int id) {
        Person person = peopleService.getPersonById(id);

        if (person != null) {
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addPerson(@RequestBody @Valid Person person,
                                       BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        peopleService.save(person);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable("id") int id,
                                          @RequestBody Person updatedPerson,
                                          BindingResult bindingResult) {
        personValidator.validate(updatedPerson, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Person person = peopleService.getPersonById(id);

        if (person != null) {
            BeanUtils.copyProperties(updatedPerson, person);
            peopleService.update(id, person);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable int id) {
        boolean isDeletedPerson = peopleService.delete(id);

        if (isDeletedPerson) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}