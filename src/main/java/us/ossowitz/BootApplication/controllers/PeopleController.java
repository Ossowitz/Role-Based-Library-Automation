package us.ossowitz.BootApplication.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

    @GetMapping("/people/{id}")
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
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        peopleService.save(person);
        return ResponseEntity.ok().build();
    }
}
