package ru.alishev.springcourse.dao;

import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    private static int PEOPLE_COUNT;
    private List<Person> people;

     {
        people = new ArrayList<>();

        people.add(new Person(++PEOPLE_COUNT, "Tom", 32, "tom@gmail.com"));
        people.add(new Person(++PEOPLE_COUNT, "Bob", 32, "bober@gmail.com"));
        people.add(new Person(++PEOPLE_COUNT, "Mike", 32, "Mouse@gmail.com"));
        people.add(new Person(++PEOPLE_COUNT, "Katy", 32, "Parry@gmail.com"));
    }

    public List<Person> index() {
         return people;
    }

    public Person show(int id) {
//         for (int i = 0; i < people.size(); i++) {
//             if (people.get(i).getId() == id) {
//                 return people.get(i);
//             }
//         };

         return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }

    public void save(Person person) {
         person.setId(++PEOPLE_COUNT);
         people.add(person); }

    public void update(int id, Person updatedPerson) {Person personToBeUpdated = show(id);
        //что будет если я просто запишу объект класса, а не буду работать с сеттерами и геттерами?
        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(updatedPerson.getEmail());

    }

    public void delete(int id) {
         people.removeIf(person -> person.getId() == id);
    }
}
