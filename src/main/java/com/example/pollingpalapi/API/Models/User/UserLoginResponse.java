package com.example.pollingpalapi.API.Models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponse {
    private User user;
    private String token;
}
