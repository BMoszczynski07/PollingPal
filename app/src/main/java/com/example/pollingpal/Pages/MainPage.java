package com.example.pollingpal.Pages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pollingpal.MainActivity;
import com.example.pollingpal.Models.Poll;
import com.example.pollingpal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainPage extends MainActivity {
    int minusDays = 14;
    ArrayList<Poll> pollsList = new ArrayList<>();
    private Context context;

    public MainPage(Context context) {
        // TODO: fetch to API, convert list into ArrayList and add polls
        this.context = context;
    }

    public void appendPollsToLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout sitePollsContainer = ((Activity) context).findViewById(R.id.site_polls_container);

        for (Poll poll : pollsList) {
            View pollLayout = inflater.inflate(R.layout.poll, sitePollsContainer, false);

            TextView pollUser = pollLayout.findViewById(R.id.poll_user);
            TextView pollDate = pollLayout.findViewById(R.id.poll_date);

            pollUser.setText(poll.user);
            pollDate.setText(poll.poll_date);

            sitePollsContainer.addView(pollLayout);
        }
    }

    public void fetchPolls() {
        LinearLayout site_loading = ((Activity) context).findViewById(R.id.site_loading);
        LinearLayout site_db_error = ((Activity) context).findViewById(R.id.site_db_error);

        site_loading.setVisibility(View.GONE);

        String requestURL = API + "/get-polls/" + minusDays;

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int httpCode = response.getInt("http");

                                if (httpCode == 200) {
                                    JSONArray res = response.getJSONArray("res");

                                    for (int i = 0; i < res.length(); i++) {
                                        JSONObject resPoll = res.getJSONObject(i);
                                        Poll pollElem = new Poll(resPoll);

                                        pollsList.add(pollElem);
                                    }

                                    appendPollsToLayout();
                                } else {

                                }
                            } catch (JSONException e) {
                                Log.d("JSONException", e.toString());
                                site_db_error.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Obsługa błędu w przypadku nieudanej odpowiedzi z serwera
                            Log.d("error2", error.toString());
                            site_db_error.setVisibility(View.VISIBLE);
                        }
                    });

            // Dodanie żądania do kolejki
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            Log.d("error3", e.toString());
            site_db_error.setVisibility(View.VISIBLE);
        }
    }
}
