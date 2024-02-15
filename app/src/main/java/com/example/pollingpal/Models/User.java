package com.example.pollingpal.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public int id;
    public String unique_id;
    public String username;
    public String email;
    public String profile_pic;
    public String create_date;

    public User(JSONObject obj) {
        try {
            this.id = obj.getInt("id");
            this.unique_id = obj.getString("unique_id");
            this.username = obj.getString("username");
            this.email = obj.getString("email");
            this.profile_pic = obj.getString("profile_pic");
            this.create_date = obj.getString("create_date");
        } catch (JSONException e) {
            Log.d("error", e.toString());
            e.printStackTrace();
        }
    }
}
