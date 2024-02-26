package com.example.pollingpalapi.API.Models.Polls;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vote {
    public int id;
    public int user_id;
    public int option_id;
    public String poll_id;
}
