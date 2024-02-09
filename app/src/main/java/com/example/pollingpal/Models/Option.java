package com.example.pollingpal.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Option {
    public int id;
    public int poll_id;
    public String poll_option;

    public Option(JSONObject obj) {
        try {
            this.id = obj.getInt("id");
            this.poll_id = obj.getInt("poll_id");
            this.poll_option = obj.getString("poll_option");
        } catch (JSONException e) {
            Log.d("error", e.toString());
            e.printStackTrace();
        }
    }
}
