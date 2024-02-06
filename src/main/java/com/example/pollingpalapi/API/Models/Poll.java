package com.example.pollingpalapi.API.Models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Poll {
    public int id;
    public int user_id;
    public String user;
    public String poll_date;
    public String poll_question;
    public int poll_hearts;
    public int poll_comments;
    public String profile_pic;
}
