package com.example.pollingpal.Models;

public class Poll {
    public int id;
    public int user_id;
    public String poll_question;
    public int poll_hearts;
    public int poll_comments;

    public Poll(int id, int user_id, String poll_question, int poll_hearts, int poll_comments) {
        this.id = id;
        this.user_id = user_id;
        this.poll_question = poll_question;
        this.poll_hearts = poll_hearts;
        this.poll_comments = poll_comments;
    }
}
