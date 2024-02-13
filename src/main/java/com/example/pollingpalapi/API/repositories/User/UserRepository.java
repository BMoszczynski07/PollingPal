package com.example.pollingpalapi.API.repositories.User;

import com.example.pollingpalapi.API.Mappers.Users.UsersMapper;
import com.example.pollingpalapi.API.Models.User.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<User> findUser(String login) {
        String sql = "SELECT * FROM users WHERE unique_id = ?";

        List<User> foundUsers = jdbc.query(sql, new Object[]{login}, new UsersMapper());

        return foundUsers;
    }
}
