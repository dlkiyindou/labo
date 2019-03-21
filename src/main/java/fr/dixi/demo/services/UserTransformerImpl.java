package fr.dixi.demo.services;

import fr.dixi.demo.enums.Gender;
import fr.dixi.demo.repositories.PersonRepository;
import fr.dixi.demo.request.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserTransformerImpl implements UserTransformer {
    @Autowired
    private PersonRepository personRepository;

    public Person entityToRequestModel(fr.dixi.demo.entities.Person entity) {
        return entityToRequestModel(entity, true);
    }

    public Person entityToRequestModel(fr.dixi.demo.entities.Person entity, boolean loadChildren) {
        if (entity == null) {
            return null;
        }

        Set<Person> children = null;
        if (loadChildren && entity.getChildren() != null) {
            children = entity.getChildren().stream().map(p -> entityToRequestModel(p, false))
                    .collect(Collectors.toSet());
        }

        return new Person(
            entity.getId(),
            entity.getLastname(),
            entity.getFirstname(),
            entity.getBirthday(),
            entity.getGender(),
            entityToRequestModel(entity.getMother(), false),
            entityToRequestModel(entity.getFather(), false),
            children
        );
    }

    public fr.dixi.demo.entities.Person modelRequestToEntity(Person person) {
        if (person == null) {
            return null;
        }

        fr.dixi.demo.entities.Person entity;
        if (person.getId() == null)
            entity = new fr.dixi.demo.entities.Person();
        else entity = personRepository.findById(person.getId()).orElseGet(fr.dixi.demo.entities.Person::new);

        return updateEntity(entity, person);
    }


    private fr.dixi.demo.entities.Person updateEntity(fr.dixi.demo.entities.Person entity, Person person) {
        if (!StringUtils.isEmpty(person.getFirstname())) {
            entity.setFirstname(person.getFirstname());
        }

        if (!StringUtils.isEmpty(person.getLastname())) {
            entity.setLastname(person.getLastname());
        }

        if (Gender.FEMALE.equals(person.getGender())) {
            entity.setGender(Gender.FEMALE);
        } else if (Gender.MALE.equals(person.getGender())) {
            entity.setGender(Gender.MALE);
        }

        if (person.getBirthday() != null) {
            entity.setBirthday(person.getBirthday());
        }

        if (person.getFather() != null) {
            entity.setFather(modelRequestToEntity(person.getFather()));
        }

        if (person.getMother() != null) {
            entity.setMother(modelRequestToEntity(person.getMother()));
        }


        return entity;
    }
}
