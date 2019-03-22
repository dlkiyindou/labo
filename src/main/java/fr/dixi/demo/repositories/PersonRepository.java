package fr.dixi.demo.repositories;

import fr.dixi.demo.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    Collection<Person> findDistinctByFirstnameIsContainingIgnoreCaseOrLastnameIsContainingIgnoreCase(String fname, String lname);

}
