package us.ossowitz.BootApplication.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(description = "Контроллер для работы с People")
public class PeopleController {

    private final PeopleService peopleService;

    private final PersonValidator personValidator;

    @GetMapping
    @ApiOperation("Получение списка всех people")
    public ResponseEntity<List<Person>> getPeople() {
        List<Person> people = peopleService.findAll();
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Получение person по индексу")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") int id) {
        Person person = peopleService.getPersonById(id);

        return (person != null)
                ? new ResponseEntity<>(person, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @ApiOperation("Добавление person в базу данных")
    public ResponseEntity<?> addPerson(@RequestBody @Valid Person person,
                                       BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        peopleService.save(person);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ApiOperation("Обновление person в базе данных")
    public ResponseEntity<?> updatePerson(@PathVariable("id") int id,
                                          @RequestBody @Valid Person updatedPerson,
                                          BindingResult bindingResult) {
        personValidator.validate(updatedPerson, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Person person = peopleService.getPersonById(id);

        if (person != null) {
            BeanUtils.copyProperties(updatedPerson, person);
            peopleService.update(id, person);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаление person из базы данных")
    public ResponseEntity<?> deletePerson(@PathVariable("id") int id) {
        boolean isDeletedPerson = peopleService.delete(id);
        return isDeletedPerson
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}