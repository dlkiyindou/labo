package fr.dixi.demo.request.model;

import fr.dixi.demo.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private Long id;

    private String lastname;

    private String firstname;

    private Date birthday;

    private Gender gender;

    private Person mother;

    private Person father;

    private Set<Person> children;

}
