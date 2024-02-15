package com.example.pollingpal.Pages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pollingpal.MainActivity;
import com.example.pollingpal.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends MainPage {
    private Context context;

    public LoginPage(Context context) {
        this.context = context;

        View mainPageView = ((Activity) context).findViewById(R.id.site_main_page);
        View commentsSection = ((Activity) context).findViewById(R.id.site_comments_section);
        View loginPage = ((Activity) context).findViewById(R.id.site_login_page);

        loginPage.setVisibility(View.VISIBLE);
        commentsSection.setVisibility(View.GONE);
        mainPageView.setVisibility(View.GONE);

        TextView returnToMainPage = ((Activity) context).findViewById(R.id.site_return_to_main_page);

        Button userLoginBtn = ((Activity) context).findViewById(R.id.site_user_login);

        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView loginInput = ((Activity) context).findViewById(R.id.site_login_input);
                TextView passInput = ((Activity) context).findViewById(R.id.site_pass_input);

                String login = loginInput.getText().toString();
                String pass = passInput.getText().toString();

                if (login == "" || pass == "") return;

                userLogin(view, login, pass);
            }
        });

        returnToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPage = new MainPage(context);
            }
        });
    }

    public void userLogin(View view, String login, String pass) {
        JSONObject user = new JSONObject();

        try {
            user.put("login", login);
            user.put("pass", pass);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException", e.toString());
        }

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String requestURL = API + "/users/login";

            JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.POST, requestURL, user,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int httpCode = response.getInt("http");

                                switch (httpCode) {
                                    case 200:
                                        // Everything OK, you can redirect to the main page and log user
                                        break;
                                    case 404:
                                        // User not found display it on the screen
                                        break;
                                    case 401:
                                        // Invalid login data
                                        break;
                                    default:
                                        // Technical problems with database
                                        break;
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
                            Log.d("ErrorResponse", error.toString());
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }
    }
}
