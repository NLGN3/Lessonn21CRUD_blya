package ru.alishev.springcourse.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.alishev.springcourse.models.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();

        rs.next();

        person.setId(rs.getInt("id"));
        person.setName(rs.getString("Name"));
        person.setEmail(rs.getString("email"));
        person.setAge(rs.getInt("age"));
        return person;
    }
}
