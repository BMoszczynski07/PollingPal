package com.example.pollingpal.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class VoteForOption {
    public String option;
    public int votesQty;

    public VoteForOption(JSONObject obj) {
        try {
            this.option = obj.getString("option");
            this.votesQty = obj.getInt("votesQty");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException", e.toString());
        }
    }
}
