package com.example.pollingpal.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class VoteForOption {
    public int optionId;
    public String option;
    public int votesQty;
    public boolean selected;

    public VoteForOption(JSONObject obj) {
        try {
            this.optionId = obj.getInt("optionId");
            this.option = obj.getString("option");
            this.votesQty = obj.getInt("votesQty");
            this.selected = obj.getBoolean("selected");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException", e.toString());
        }
    }
}
