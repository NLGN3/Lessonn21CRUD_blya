package ru.alishev.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.alishev.springcourse.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() throws SQLException {
//        return jdbcTemplate.query("SELECT * FROM person", new PersonMapper());
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) throws SQLException {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) throws SQLException {
        jdbcTemplate.update("INSERT INTO person(name, age, email) VALUES (?, ?, ?)", person.getName(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person updatedPerson) throws SQLException {
        jdbcTemplate.update("UPDATE person SET name = ?, age = ?, email = ? WHERE id = ?", updatedPerson.getName(), updatedPerson.getAge(), updatedPerson.getEmail(), id);
    }

    public void delete(int id) throws SQLException {
        jdbcTemplate.update("DELETE FROM person WHERE id = ?", id);
    }


    public void testMultipleUpdate() {
        List<Person> people = create1000people();

        long before = System.currentTimeMillis();

        for(Person person : people) {
            jdbcTemplate.update("INSERT INTO person VALUES (?, ?, ?, ?)", person.getId(), person.getName(), person.getAge(), person.getEmail());
        }

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after-before));
    }

    public void testBatchUpdate() {
        List<Person> people = create1000people();

        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO person VALUES (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            //Этот метод будет вызываться столько раз, сколько вернем в следующем методе.
            //Логика маппера полностью на нас - получаем по айдишнику person, его данные, и кладем в нужные поля запроса
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, people.get(i).getId());
                ps.setString(2, people.get(i).getName());
                ps.setInt(3, people.get(i).getAge());
                ps.setString(4, people.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });

        long after = System.currentTimeMillis();

        System.out.println("Time: " + (after-before));

    }

    public List<Person> create1000people() {
        List<Person> people = new ArrayList<>();

        for(int i = 0; i < 1000; i++) {
            people.add(new Person(i, "Name" + i, 30, "name"+i+"@mail.ru"));
        }
        return people;
    }
}
