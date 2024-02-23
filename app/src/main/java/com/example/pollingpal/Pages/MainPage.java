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
import com.example.pollingpal.Models.User;
import com.example.pollingpal.Models.VoteForOption;
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
    ArrayList<Integer> likedPollsIds = new ArrayList<>();
    public User user;

    public MainPage() {}

    public MainPage(Context context, User user) {
        this.user = user;

        // TODO: fetch to API, convert list into ArrayList and add polls
        this.context = context;

        View mainPageView = ((Activity) context).findViewById(R.id.site_main_page);
        View loginPageView = ((Activity) context).findViewById(R.id.site_login_page);

        loginPageView.setVisibility(View.GONE);
        mainPageView.setVisibility(View.VISIBLE);

        Log.d("redirecting", "redirecting to main page...");

        Button searchBtn = ((Activity) context).findViewById(R.id.search_polls_btn);
        Button loginBtn = ((Activity) context).findViewById(R.id.site_login);

        fetchPolls();

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

    public void appendVotes(LinearLayout optionsContainer, ArrayList<VoteForOption> votes) {
        optionsContainer.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (votes.isEmpty()) {
            View optionSelectedView = inflater.inflate(R.layout.no_votes, optionsContainer, false);

            optionsContainer.addView(optionSelectedView);

            return;
        }

        int votesQty = 0;

        for (VoteForOption vote : votes)
            votesQty += vote.votesQty;

        for (VoteForOption vote : votes) {
            View optionSelectedView = inflater.inflate(R.layout.poll_selected, optionsContainer, false);

            double optionPercentage = Math.floor(((float) vote.votesQty / votesQty) * 100);

            TextView optionSelectedText = optionSelectedView.findViewById(R.id.poll_option_selected_text);
            optionSelectedText.setText(vote.option + " · " + optionPercentage + "%");

            View optionBar = optionSelectedView.findViewById(R.id.poll_option_selected_bar);

            int optionBarWidth = 200 * vote.votesQty / votesQty;

            optionBar.getLayoutParams().width = optionBarWidth;

            optionsContainer.addView(optionSelectedView);

            ImageView optionSelectedCheck = optionSelectedView.findViewById(R.id.poll_option_selected_check);

            if (!vote.selected) optionSelectedCheck.setVisibility(View.GONE);
        }
    }

    public void getVotes(LinearLayout optionsContainer, int pollId) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String requestURL = user != null ? API + "/get-votes-for-user/" + pollId + "/" + user.id :
                    API + "/get-votes/" + pollId;

            JsonObjectRequest voteReq = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int httpCode = response.getInt("http");

                                if (httpCode == 200) {
                                    JSONArray res = response.getJSONArray("res");
                                    ArrayList<VoteForOption> votes = new ArrayList<>();

                                    for (int i = 0; i < res.length(); i++) {
                                        VoteForOption vote = new VoteForOption(res.getJSONObject(i));

                                        votes.add(vote);
                                    }

                                    appendVotes(optionsContainer, votes);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("JSONException", e.toString());
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.d("Error", error.toString());
                        }
                    }
            );

            requestQueue.add(voteReq);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error", e.toString());
        }
    }

    public void voteForOption (Option option, LinearLayout optionsContainer) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JSONObject optionObject = new JSONObject();

            String requestURL = API + "/vote-for-option";

            try {
                optionObject.put("optionId", option.id);
                optionObject.put("userId", user.id);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JSONException", e.toString());
            }

            JsonObjectRequest voteReq = new JsonObjectRequest(Request.Method.POST, requestURL, optionObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int httpCode = response.getInt("http");

                                if (httpCode == 200) {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("JSONException", e.toString());
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.d("Error", error.toString());
                        }
                    }
            );

            requestQueue.add(voteReq);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", e.toString());
        }
    }

    public void appendOptions(LinearLayout optionsContainer, ArrayList<Option> optionsArray) {
        Log.d("options", optionsArray.toString());

        for (Option option : optionsArray) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View optionView = inflater.inflate(R.layout.poll_select, optionsContainer, false);

            TextView optionText = optionView.findViewById(R.id.poll_option_text);
            optionText.setText(option.poll_option);

            optionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    voteForOption(option, optionsContainer);
                }
            });

            optionsContainer.addView(optionView);
        }
    }

    public void fetchOptions(LinearLayout optionsContainer, int pollId) {
//        TODO: Fetch all options from the database using polls.id and return array of options
        optionsContainer.removeAllViews();

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
                                    ArrayList<Option> options = new ArrayList<>();

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

    public void likePoll(View pollLayout, int pollId) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JSONObject pollLike = new JSONObject();

            String requestURL = API + "/like-poll";

            try {
                pollLike.put("pollId", pollId);
                pollLike.put("userId", user.id);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JSONException", e.toString());
            }

            JsonObjectRequest likeReq = new JsonObjectRequest(Request.Method.POST, requestURL, pollLike,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int httpCode = response.getInt("http");

                                if (httpCode == 200) {
                                    TextView pollLikes = pollLayout.findViewById(R.id.poll_hearts);

                                    int like = response.getInt("res");

                                    if (like == 1) {
                                        pollLikes.setText(String.valueOf(Integer.parseInt(pollLikes.getText().toString()) + 1));
                                        return;
                                    }

                                    pollLikes.setText(String.valueOf(Integer.parseInt(pollLikes.getText().toString()) - 1));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("JSONException", e.toString());
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.d("volleyError", error.toString());
                        }
                    }
            );

            requestQueue.add(likeReq);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error", e.toString());
        }
    }

    public boolean didUserVote(int pollId, int userId) {
        final boolean[] voted = {true};

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String requestURL = API + "/did-user-vote/" + pollId + "/" + userId;

            JsonObjectRequest didUserVoteReq = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int httpCode = response.getInt("http");

                                if (httpCode == 200) {
                                    voted[0] = response.getBoolean("res");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("JSONException", e.toString());
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.d("API error", error.toString());
                        }
                    }
            );

            requestQueue.add(didUserVoteReq);

            return voted[0];

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", e.toString());
        }

        return false;
    }

    public void appendPollsToLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout sitePollsContainer = ((Activity) context).findViewById(R.id.site_polls_container);

        sitePollsContainer.removeAllViews();

        for (Poll poll : pollsList) {
            View pollLayout = inflater.inflate(R.layout.poll, sitePollsContainer, false);

            ImageView pollPic = pollLayout.findViewById(R.id.poll_pic);
            TextView pollUser = pollLayout.findViewById(R.id.poll_user);
            TextView pollDate = pollLayout.findViewById(R.id.poll_date);
            TextView pollQuestion = pollLayout.findViewById(R.id.poll_question);
            TextView pollHearts = pollLayout.findViewById(R.id.poll_hearts);
            TextView pollComments = pollLayout.findViewById(R.id.poll_comments);

            LinearLayout pollHeartsContainer = pollLayout.findViewById(R.id.poll_hearts_container);
            LinearLayout pollCommentsContainer = pollLayout.findViewById(R.id.poll_comments_container);

            Picasso.get().load(poll.profile_pic).into(pollPic);
            pollUser.setText(poll.user);
            pollDate.setText(poll.poll_date);
            pollQuestion.setText(poll.poll_question);
            pollHearts.setText(String.valueOf(poll.poll_hearts));
            pollComments.setText(String.valueOf(poll.poll_comments));

            pollHeartsContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likePoll(pollLayout, poll.id);
                }
            });

//            TODO: return the arraylist of options for polls and loop through them
            LinearLayout options = pollLayout.findViewById(R.id.poll_options);


            if (user == null) {
                fetchOptions(options, poll.id);
            } else {
                boolean didUserVote = didUserVote(poll.id, user.id);

                if (!didUserVote) {
                    fetchOptions(options, poll.id);
                } else {
                    getVotes(options, poll.id);
                }
            }

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
