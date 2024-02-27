package com.example.pollingpalapi.API.Models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDTO {
    private String uniqueId;
    private String username;
    private String pass;
    private String email;
}
