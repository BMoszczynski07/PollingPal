package com.example.pollingpalapi.API.Models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private int id;
    private String unique_id;
    private String username;
    private String pass;
    private String email;
    private String profile_pic;
    private String create_date;
    private boolean verified;
}
