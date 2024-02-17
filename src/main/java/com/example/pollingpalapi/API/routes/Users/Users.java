package com.example.pollingpalapi.API.routes.Users;

import com.example.pollingpalapi.API.Models.Response.Response;
import com.example.pollingpalapi.API.Models.User.User;
import com.example.pollingpalapi.API.Models.User.UserLoginDTO;
import com.example.pollingpalapi.API.Utils.PasswordUtil;
import com.example.pollingpalapi.API.repositories.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Users {
    private final UserRepository user;

    @Autowired
    public Users(UserRepository user) {
        this.user = user;
    }

    @PostMapping("/users/login")
    public Response userLogin(@RequestBody UserLoginDTO userDTO) {
        try {
            List<User> foundUsers = user.findUser(userDTO.login);

            if (foundUsers.size() == 0) {
                return new Response<String>(404, "Nie znaleziono użytkownika o takich danych");
            }

            if (!PasswordUtil.matchPassword(userDTO.pass, foundUsers.get(0).getPass())) {
                return new Response<String>(401, "Podano błędne dane logowania");
            }

            return new Response<List<User>>(200, foundUsers);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<String>(500, "Wystąpił błąd techniczny!");
        }
    }
}
