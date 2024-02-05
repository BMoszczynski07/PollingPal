package com.example.pollingpal.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Poll {
    public int id;
    public int user_id;
    public String user;
    public String poll_question;
    public int poll_hearts;
    public int poll_comments;

    public Poll(JSONObject obj) {
        try {
            this.id = obj.getInt("id");
            this.user_id = obj.getInt("user_id");
            this.user = obj.getString("user");
            this.poll_question = obj.getString("poll_question");
            this.poll_hearts = obj.getInt("poll_hearts");
            this.poll_comments = obj.getInt("poll_comments");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
