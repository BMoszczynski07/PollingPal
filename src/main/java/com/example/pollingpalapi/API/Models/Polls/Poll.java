package com.example.pollingpalapi.API.Models.Polls;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Poll {
    private int id;
    private int user_id;
    private String user;
    private String poll_date;
    private String poll_question;
    private int poll_hearts;
    private int poll_comments;
    private String profile_pic;
}
