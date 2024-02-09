package com.example.pollingpal.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Poll {
    public int id;
    public int user_id;
    public String user;
    public String poll_date;
    public String poll_question;
    public int poll_hearts;
    public int poll_comments;
    public String profile_pic;

    public Poll(JSONObject obj) {
        try {
            this.id = obj.getInt("id");
            this.user_id = obj.getInt("user_id");
            this.user = obj.getString("user");
            this.poll_question = obj.getString("poll_question");
            this.profile_pic = obj.getString("profile_pic");
            this.poll_hearts = obj.getInt("poll_hearts");
            this.poll_comments = obj.getInt("poll_comments");
            this.poll_date = obj.getString("poll_date");
        } catch (JSONException e) {
            Log.d("error", e.toString());
            e.printStackTrace();
        }
    }
}
