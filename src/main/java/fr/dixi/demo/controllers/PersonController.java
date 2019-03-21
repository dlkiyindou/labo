package fr.dixi.demo.controllers;

import fr.dixi.demo.repositories.PersonRepository;
import fr.dixi.demo.request.model.Person;
import fr.dixi.demo.services.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = {"http://127.0.0.1:4200", "http://localhost:4200"}, maxAge=3600)
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserTransformer userTransformer;

    @PostMapping(value = {"", "/", "/create"}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    Person create(@RequestBody Person person) {
        fr.dixi.demo.entities.Person entity = userTransformer.modelRequestToEntity(person);

        return userTransformer.entityToRequestModel(personRepository.save(entity));
    }

    @GetMapping(value = {"", "/", "/read"})
    Set<Person> read() {
        Set<Person> response;
        response = new TreeSet<>(Comparator.comparing(p -> p.getId()));
        Iterator<fr.dixi.demo.entities.Person> it = personRepository.findAll().iterator();

        while (it.hasNext()) {
            Person p = userTransformer.entityToRequestModel(it.next());
            response.add(p);
        }

        return response;
    }

    @GetMapping(value = {"/{id}", "/read/{id}"})
    Person read(@PathVariable("id") Long id) {
        Optional<fr.dixi.demo.entities.Person> optionalPerson  = personRepository.findById(id);
        return optionalPerson.isPresent() ? userTransformer.entityToRequestModel(optionalPerson.get()) : null;
    }

    @PutMapping(value = {"", "/", "/update"}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    Person update(@RequestBody Person person) {
        fr.dixi.demo.entities.Person entity = userTransformer.modelRequestToEntity(person);

        return userTransformer.entityToRequestModel(personRepository.save(entity));
    }

    @DeleteMapping(value = {"/{id}", "/delete/{id}"})
    void delete(@PathVariable("id") Long id) {
        personRepository.deleteById(id);
    }
}
