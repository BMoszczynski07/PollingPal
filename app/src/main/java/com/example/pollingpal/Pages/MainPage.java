package com.example.pollingpal.Pages;

import android.opengl.Visibility;
import android.view.View;
import android.widget.LinearLayout;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainPage extends MainActivity {
    ArrayList<Poll> pollsList;

    public MainPage() {
        // TODO: fetch to API, convert list into ArrayList and add polls
        fetchPolls();
    }

    public void appendPollsToLayout() {

    }

    public void fetchPolls() {
        LinearLayout site_loading = (LinearLayout) findViewById(R.id.site_loading);

        URL requestURL = new URL("http://localhost:8000");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Obsługa odpowiedzi z serwera
                            // Przetwarzanie odpowiedzi -> zamień na ArrayList i dodaj do listy pollsList
                            site_loading.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Obsługa błędu w przypadku nieudanej odpowiedzi z serwera
                        error.printStackTrace();
                    }
                });

        // Dodanie żądania do kolejki
        requestQueue.add(jsonArrayRequest);
    }
}
