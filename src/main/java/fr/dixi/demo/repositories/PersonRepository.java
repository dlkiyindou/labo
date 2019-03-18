package fr.dixi.demo.repositories;

import fr.dixi.demo.entities.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Collection<Person> findByLastname(String lastname);

    Collection<Person> findByFirstname(String firstname);
}
