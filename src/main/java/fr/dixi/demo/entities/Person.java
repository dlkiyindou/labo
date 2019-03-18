package fr.dixi.demo.entities;

import fr.dixi.demo.enums.Gender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table(name = "person")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    private String lastname;

    private String firstname;

    private Date birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "mother_id")
    private Person mother;

    @ManyToOne
    @JoinColumn(name = "father_id")
    private Person father;

    @OneToMany(mappedBy = "mother")
    @Getter(AccessLevel.NONE)
    private Set<Person> childrenIfMother;

    @OneToMany(mappedBy = "father")
    @Getter(AccessLevel.NONE)
    private Set<Person> childrenIfFather;

    public Set<Person> getChildren() {
        if (Gender.FEMALE.equals(gender)) {
            return childrenIfMother;
        }

        return childrenIfFather;
    }
}
