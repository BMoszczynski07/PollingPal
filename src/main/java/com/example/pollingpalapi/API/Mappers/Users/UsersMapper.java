package com.example.pollingpalapi.API.Mappers.Users;

import com.example.pollingpalapi.API.Models.Polls.Poll;
import com.example.pollingpalapi.API.Models.User.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setUnique_id(resultSet.getString("unique_id"));
        user.setUsername(resultSet.getString("username"));
        user.setPass(resultSet.getString("password"));
        user.setEmail(resultSet.getString("e-mail"));
        user.setProfile_pic(resultSet.getString("profile_pic"));
        user.setCreate_date(resultSet.getString("create_date"));
        user.setVerified(resultSet.getBoolean("verified"));

        return user;
    }
}
