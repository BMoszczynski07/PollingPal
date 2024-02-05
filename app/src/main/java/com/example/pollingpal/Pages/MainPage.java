package com.example.pollingpal.Pages;

import android.app.Activity;
import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.pollingpal.MainActivity;
import com.example.pollingpal.Models.Poll;
import com.example.pollingpal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainPage extends MainActivity {
    ArrayList<Poll> pollsList;
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
        }
    }

    public void fetchPolls() {
        LinearLayout site_loading = ((Activity) context).findViewById(R.id.site_loading);
        LinearLayout site_db_error = ((Activity) context).findViewById(R.id.site_db_error);

        site_loading.setVisibility(View.GONE);

        String requestURL = API + "/get-polls";

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            // Obsługa odpowiedzi z serwera
                            // Przetwarzanie odpowiedzi -> zamień na ArrayList i dodaj do listy pollsList

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject pollObj = response.getJSONObject(i);
                                    Poll pollElem = new Poll(pollObj);

                                    pollsList.add(pollElem);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    site_db_error.setVisibility(View.VISIBLE);
                                }
                            }

                            appendPollsToLayout();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Obsługa błędu w przypadku nieudanej odpowiedzi z serwera
                            error.printStackTrace();
                            site_db_error.setVisibility(View.VISIBLE);
                        }
                    });

            // Dodanie żądania do kolejki
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            site_db_error.setVisibility(View.VISIBLE);
        }
    }
}
