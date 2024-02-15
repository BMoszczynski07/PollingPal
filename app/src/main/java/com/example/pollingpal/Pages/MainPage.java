package com.example.pollingpal.Pages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pollingpal.MainActivity;
import com.example.pollingpal.Models.Option;
import com.example.pollingpal.Models.Poll;
import com.example.pollingpal.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainPage extends MainActivity {
    int minusDays = 14;
    ArrayList<Poll> pollsList = new ArrayList<>();
    private Context context;
    public LoginPage loginPage;

    public MainPage() {}

    public MainPage(Context context) {
        // TODO: fetch to API, convert list into ArrayList and add polls
        this.context = context;

        View mainPageView = ((Activity) context).findViewById(R.id.site_main_page);
        View commentsSection = ((Activity) context).findViewById(R.id.site_comments_section);
        View loginPageView = ((Activity) context).findViewById(R.id.site_login_page);

        loginPageView.setVisibility(View.GONE);
        commentsSection.setVisibility(View.GONE);
        mainPageView.setVisibility(View.VISIBLE);

        Button searchBtn = ((Activity) context).findViewById(R.id.search_polls_btn);
        Button loginBtn = ((Activity) context).findViewById(R.id.site_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPage = new LoginPage(context);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPolls(view);
            }
        });
    }

    public void searchPolls(View view) {
        EditText searchInput = ((Activity) context).findViewById(R.id.search_polls);
        String searchText = searchInput.getText().toString();

        View site_db_error = ((Activity) context).findViewById(R.id.site_db_error);
        LinearLayout pollsContainer = ((Activity) context).findViewById(R.id.site_polls_container);

        View pollsLoading = ((Activity) context).findViewById(R.id.site_loading);

        pollsContainer.removeAllViews();
        site_db_error.setVisibility(View.GONE);
        pollsLoading.setVisibility(View.VISIBLE);

        pollsList.clear();

        Log.d("searching polls", "searching polls...");

        JSONObject poll = new JSONObject();

        try {
            poll.put("content", searchText);
        } catch (JSONException e) {
            Log.d("jsonexceptionerror", e.toString());
        }

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String requestURL = API + "/search-polls";

            Log.d("doing request", "doing request...");

            JsonObjectRequest pollsReq = new JsonObjectRequest(Request.Method.POST, requestURL, poll,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("response", "response");

                                pollsLoading.setVisibility(View.GONE);

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
                                    Log.d("err res", response.toString());
                                    pollsLoading.setVisibility(View.GONE);
                                    site_db_error.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                Log.d("JSONException", e.toString());
                                pollsLoading.setVisibility(View.GONE);
                                site_db_error.setVisibility(View.VISIBLE);
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("res err", error.toString());
                            pollsLoading.setVisibility(View.GONE);
                            site_db_error.setVisibility(View.VISIBLE);
                        }
                    }
            );

            requestQueue.add(pollsReq);
        } catch (Exception e) {
            Log.d("jsonexception", e.toString());
            pollsLoading.setVisibility(View.GONE);
            site_db_error.setVisibility(View.VISIBLE);
        }

        Log.d("click", "click");
    }

    public void appendOptions(LinearLayout optionsContainer, ArrayList<Option> optionsArray) {
        Log.d("options", optionsArray.toString());

        for (Option option : optionsArray) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View optionView = inflater.inflate(R.layout.poll_select, optionsContainer, false);

            TextView optionText = optionView.findViewById(R.id.poll_option_text);
            optionText.setText(option.poll_option);

            optionsContainer.addView(optionView);
        }
    }

    public void fetchOptions(LinearLayout optionsContainer, int pollId) {
//        TODO: Fetch all options from the database using polls.id and return array of options
        ArrayList<Option> options = new ArrayList<>();

        String requestURL = API + "/get-poll-options/" + pollId;

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest optionsReq = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int httpCode = response.getInt("http");

                                Log.d("res", response.toString());

                                if (httpCode == 200) {
                                    JSONArray allOptions = response.getJSONArray("res");

                                    for (int i = 0; i < allOptions.length(); i++) {
                                        JSONObject option = allOptions.getJSONObject(i);
                                        Option newOption = new Option(option);

                                        options.add(newOption);
                                    }

                                    Log.d("appending options", "appending options");

                                    appendOptions(optionsContainer, options);
                                } else {
                                    Log.d("DB Error", response.toString());
                                }
                            } catch (JSONException e) {
                                Log.d("jsonexception error", e.toString());
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("request error", error.toString());
                        }
                    }
            );

            requestQueue.add(optionsReq);
        } catch (Exception e) {
            Log.d("jsonexception error", e.toString());
        }
    }

    public void appendPollsToLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout sitePollsContainer = ((Activity) context).findViewById(R.id.site_polls_container);

        for (Poll poll : pollsList) {
            View pollLayout = inflater.inflate(R.layout.poll, sitePollsContainer, false);

            ImageView pollPic = pollLayout.findViewById(R.id.poll_pic);
            TextView pollUser = pollLayout.findViewById(R.id.poll_user);
            TextView pollDate = pollLayout.findViewById(R.id.poll_date);
            TextView pollQuestion = pollLayout.findViewById(R.id.poll_question);

            Picasso.get().load(poll.profile_pic).into(pollPic);
            pollUser.setText(poll.user);
            pollDate.setText(poll.poll_date);
            pollQuestion.setText(poll.poll_question);

//            TODO: return the arraylist of options for polls and loop through them
            LinearLayout options = pollLayout.findViewById(R.id.poll_options);

            // if (user nie zaznaczył opcji w ankiecie) {
            fetchOptions(options, poll.id);
            // } else if (user zaznaczył opcję w ankiecie) {

            // }

//            for (Option option : pollOptions) {
//                View pollOptionLayout = inflater.inflate(R.layout.poll_select, sitePollsContainer, false);
//
//                TextView pollOption = pollOption.findViewById(R.id.poll_option);
//
//                pollOption.setText(option.poll_option);
//            }

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
                                    Log.d("err res", response.toString());
                                    site_db_error.setVisibility(View.VISIBLE);
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
