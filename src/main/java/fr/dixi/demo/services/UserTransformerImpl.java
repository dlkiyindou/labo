package fr.dixi.demo.services;

import fr.dixi.demo.enums.Gender;
import fr.dixi.demo.repositories.PersonRepository;
import fr.dixi.demo.request.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserTransformerImpl implements UserTransformer {
    @Autowired
    private PersonRepository personRepository;

    public Person entityToRequestModel(fr.dixi.demo.entities.Person entity) {
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

    public fr.dixi.demo.entities.Person modelRequestToEntity(Person person) {
        if (person == null) {
            return null;
        }

        fr.dixi.demo.entities.Person entity = personRepository.findById(person.getId()).orElseGet(fr.dixi.demo.entities.Person::new);

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

        if (person.getChildren() != null) {
            Set<fr.dixi.demo.entities.Person> children = new HashSet<>();
            for (Person child : person.getChildren()) {
                fr.dixi.demo.entities.Person childEntity = modelRequestToEntity(child);
                if (childEntity != null) {
                    if (Gender.FEMALE.equals(entity.getGender())) {
                        childEntity.setMother(entity);
                        children.add(childEntity);
                    } else if (Gender.MALE.equals(entity.getGender())) {
                        childEntity.setFather(entity);
                        children.add(childEntity);
                    }
                }
            }

            if (Gender.FEMALE.equals(entity.getGender())) {
                entity.setChildrenIfMother(children);
                entity.setChildrenIfFather(null);
            } else if (Gender.MALE.equals(entity.getGender())) {
                entity.setChildrenIfFather(children);
                entity.setChildrenIfMother(null);
            }
        }

        return new fr.dixi.demo.entities.Person();
    }
}
