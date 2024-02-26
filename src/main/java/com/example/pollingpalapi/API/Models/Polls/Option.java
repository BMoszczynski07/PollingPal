package com.example.pollingpalapi.API.Models.Polls;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Option {
    private int id;
    private String poll_id;
    private String poll_option;
}
