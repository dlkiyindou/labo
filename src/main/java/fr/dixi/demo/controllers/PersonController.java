package fr.dixi.demo.controllers;

import fr.dixi.demo.enums.Gender;
import fr.dixi.demo.repositories.PersonRepository;
import fr.dixi.demo.request.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @RequestMapping("/{id}")
    Person get(@PathVariable("id") Long id) {
        fr.dixi.demo.entities.Person entity  = personRepository.findById(id).orElseGet(fr.dixi.demo.entities.Person::new);
        return entityToRequestModel(entity);
    }

    @PostMapping(value = "/put/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    void put(@PathVariable(value = "id") Long id, @RequestBody Person person) {
        fr.dixi.demo.entities.Person entity = modelRequestToEntity(person);

        personRepository.save(entity);

    }

    @PostMapping(value = "/put", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    void put(@RequestBody Person person) {
        fr.dixi.demo.entities.Person entity = modelRequestToEntity(person);

        personRepository.save(entity);

    }

    private Person entityToRequestModel(fr.dixi.demo.entities.Person entity) {
        if (entity == null) {
            return null;
        }

        return new Person(
                entity.getId(),
                entity.getLastname(),
                entity.getFirstname(),
                entity.getBirthday(),
                entity.getGender(),
                entityToRequestModel(entity.getMother()),
                entityToRequestModel(entity.getFather()),
                entity.getChildren() == null ? null : entity.getChildren().stream().map(p -> entityToRequestModel(p)).collect(Collectors.toSet())
        );
    }

    private fr.dixi.demo.entities.Person modelRequestToEntity(Person person) {
        if (person == null) {
            return null;
        }

        fr.dixi.demo.entities.Person entity = new fr.dixi.demo.entities.Person();
        entity.setId(person.getId());
        entity.setFirstname(person.getFirstname());
        entity.setLastname(person.getLastname());
        entity.setBirthday(person.getBirthday());
        entity.setFather(modelRequestToEntity(person.getFather()));
        entity.setGender(entity.getGender());

        if (Gender.FEMALE.equals(entity.getGender())) {

        }

        return entity;
    }
}
