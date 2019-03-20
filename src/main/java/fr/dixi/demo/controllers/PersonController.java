package fr.dixi.demo.controllers;

import fr.dixi.demo.repositories.PersonRepository;
import fr.dixi.demo.request.model.Person;
import fr.dixi.demo.services.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserTransformer userTransformer;

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    Person create(@RequestBody Person person) {
        fr.dixi.demo.entities.Person entity = userTransformer.modelRequestToEntity(person);

        return userTransformer.entityToRequestModel(personRepository.save(entity));
    }

    @GetMapping(value = {"", "/", "/all"})
    Set<Person> read() {
        Set<Person> response = new HashSet<>();
        Iterator<fr.dixi.demo.entities.Person> it = personRepository.findAll().iterator();

        while (it.hasNext()) {
            Person p = userTransformer.entityToRequestModel(it.next());
            response.add(p);
        }

        return response;
    }

    @GetMapping(value = "/{id}")
    Person read(@PathVariable("id") Long id) {
        Optional<fr.dixi.demo.entities.Person> optionalPerson  = personRepository.findById(id);
        return optionalPerson.isPresent() ? userTransformer.entityToRequestModel(optionalPerson.get()) : null;
    }

    @PutMapping(value = "/update/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    Person update(@PathVariable(value = "id") Long id, @RequestBody Person person) {
        person.setId(id);
        fr.dixi.demo.entities.Person entity = userTransformer.modelRequestToEntity(person);

        return userTransformer.entityToRequestModel(personRepository.save(entity));
    }

    @DeleteMapping(value = "/delete/{id}")
    void delete(@PathVariable("id") Long id) {
        personRepository.deleteById(id);
    }
}
