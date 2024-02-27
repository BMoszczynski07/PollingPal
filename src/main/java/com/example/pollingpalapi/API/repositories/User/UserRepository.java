package com.example.pollingpalapi.API.repositories.User;

import com.example.pollingpalapi.API.Mappers.Users.UsersMapper;
import com.example.pollingpalapi.API.Models.User.User;
import com.example.pollingpalapi.API.Models.User.UserRegisterDTO;
import com.example.pollingpalapi.API.Utils.PasswordUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<User> findUser(String login) {
        String sql = "SELECT * FROM users WHERE unique_id = ? AND verified = 1";

        List<User> foundUsers = jdbc.query(sql, new Object[]{login}, new UsersMapper());

        return foundUsers;
    }

    public List<User> findRegisteredUser(UserRegisterDTO userDTO) {
        String findUserSQL = "SELECT * FROM users WHERE unique_id = ? OR `e-mail` = ?";

        return jdbc.query(findUserSQL, new UsersMapper(), new Object[]{userDTO.getUniqueId(), userDTO.getEmail()});
    }

    public void userRegister(UserRegisterDTO userDTO) {
        List<User> findUser = findRegisteredUser(userDTO);

        if (findUser.size() > 0) return;

        String encodedPassword = PasswordUtil.encodePassword(userDTO.getPass());

        Date currentDate = new Date();

        String addUserSQL = "INSERT INTO users " +
                "(`unique_id`, `username`, `password`, `e-mail`, `profile_pic`, `create_date`, `verified`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbc.update(addUserSQL, new Object[]{userDTO.getUniqueId(), userDTO.getUsername(), encodedPassword, userDTO.getEmail(), "http://192.168.122.104:8000/images/guest.png", currentDate, 1});
    }
}
