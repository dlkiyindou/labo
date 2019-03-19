package fr.dixi.demo.services;

import fr.dixi.demo.request.model.Person;

public interface UserTransformer {
    Person entityToRequestModel(fr.dixi.demo.entities.Person entity);

    fr.dixi.demo.entities.Person modelRequestToEntity(Person person);
}
