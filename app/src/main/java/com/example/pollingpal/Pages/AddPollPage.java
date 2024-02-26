package com.example.pollingpal.Pages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pollingpal.MainActivity;
import com.example.pollingpal.Models.User;
import com.example.pollingpal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddPollPage extends MainActivity {
    Context context;
    User user;
    int optionsCounter = 0;
    ArrayList<View> optionsEditTexts = new ArrayList<>();
    public LinearLayout siteEditOptionsContainer;

    public AddPollPage(Context context, User user) {
        this.context = context;
        this.user = user;

        siteEditOptionsContainer = ((Activity) context).findViewById(R.id.site_edit_options_container);

        View mainPageView = ((Activity) context).findViewById(R.id.site_main_page);
        View loginPage = ((Activity) context).findViewById(R.id.site_login_page);
        View addPollPage = ((Activity) context).findViewById(R.id.site_add_poll_page);

        loginPage.setVisibility(View.GONE);
        mainPageView.setVisibility(View.GONE);
        addPollPage.setVisibility(View.VISIBLE);

        Button siteAddOption = ((Activity) context).findViewById(R.id.site_add_option);

        EditText siteEditNickname = ((Activity) context).findViewById(R.id.site_edit_nickname);

        if (user != null) {
            siteEditNickname.setVisibility(View.GONE);
        } else {
            siteEditNickname.setVisibility(View.VISIBLE);
        }

        siteAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOption();
            }
        });

        Button sendPollButton = ((Activity) context).findViewById(R.id.site_send_poll);

        sendPollButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("adding poll...", "adding poll...");
                        addPoll();
                    }
                }
        );

        TextView returnBack = ((Activity) context).findViewById(R.id.site_return_to_main_page_add_poll);

        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siteEditOptionsContainer.removeAllViews();

                mainPage = new MainPage(context, user);
            }
        });
    }

    public void addPoll() {
        JSONObject pollPayload = new JSONObject();

        Log.d("addPoll()", "addPoll()");

        try {
            pollPayload.put("nickname", "");
            pollPayload.put("userId", user.id);

            EditText editQuestion = ((Activity) context).findViewById(R.id.site_edit_question);
            String question = editQuestion.getText().toString();

            if (question.isEmpty()) return;

            pollPayload.put("poll_question", question);

            ArrayList<String> options = new ArrayList<>();

            for (View optionsEditText : optionsEditTexts) {
                EditText editOption = ((Activity) context).findViewById(optionsEditText.getId());
                String option = editOption.getText().toString();

                if ("".equals(option)) continue;

                options.add(option);
            }

            if (options.size() < 2) return;

            JSONArray optionsArray = new JSONArray(options);

            pollPayload.put("options", optionsArray);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException", e.toString());
        }

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String requestURL = API + "/add-poll";

            Log.d("doing request...", "doing request...");

            JsonObjectRequest addPollReq = new JsonObjectRequest(Request.Method.POST, requestURL, pollPayload,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int httpCode = response.getInt("http");

                                if (httpCode == 200) {
                                    // success
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
                            Log.d("error", error.toString());
                        }
                    }
            );

            requestQueue.add(addPollReq);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", e.toString());
        }
    }

    public void addOption() {
        if (optionsCounter == 10) return;

        optionsCounter++;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View siteEdit = inflater.inflate(R.layout.site_edit_option, siteEditOptionsContainer, false);
        EditText editText = siteEdit.findViewById(R.id.site_edit_option);

        editText.setId(optionsCounter);

        editText.setHint("Opcja nr " + optionsCounter);

        optionsEditTexts.add(editText);

        siteEditOptionsContainer.addView(siteEdit);

        Log.d("options", optionsEditTexts.toString());
    }
}
