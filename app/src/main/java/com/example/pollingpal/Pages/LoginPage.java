package com.example.pollingpal.Pages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends MainActivity {
    private Context context;

    public LoginPage(Context context) {
        this.context = context;

        View mainPageView = ((Activity) context).findViewById(R.id.site_main_page);
        View loginPage = ((Activity) context).findViewById(R.id.site_login_page);

        loginPage.setVisibility(View.VISIBLE);
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

                Log.d("login", login);
                Log.d("pass", pass);

                if (login.equals("") || pass.equals("")) return;

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
        JSONObject userJSON = new JSONObject();

        try {
            userJSON.put("login", login);
            userJSON.put("pass", pass);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException", e.toString());
        }

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String requestURL = API + "/users/login";

            JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.POST, requestURL, userJSON,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int httpCode = response.getInt("http");

                                View siteLoginError = ((Activity) context).findViewById(R.id.site_login_error);
                                TextView siteLoginErrorText = ((Activity) context).findViewById(R.id.site_login_error_text);

                                siteLoginError.setVisibility(View.VISIBLE);

                                switch (httpCode) {
                                    case 200:
                                        // Everything OK, you can redirect to the main page and log user
                                        siteLoginError.setVisibility(View.GONE);

                                        JSONArray res = response.getJSONArray("res");
                                        user = new User(res.getJSONObject(0));

                                        Log.d("res", res.toString());
                                        Log.d("resObj", res.getJSONObject(0).toString());
                                        Log.d("userObj", user != null ? user.toString() : "null");

                                        mainPage = new MainPage(context);

                                        View loginBtns = ((Activity) context).findViewById(R.id.site_login_btns);
                                        View userLogged = ((Activity) context).findViewById(R.id.site_user_logged);

                                        if (user != null) {
                                            loginBtns.setVisibility(View.GONE);
                                            userLogged.setVisibility(View.VISIBLE);

                                            View siteUserLogged = ((Activity) context).findViewById(R.id.site_user_logged);

                                            ImageView siteUserPic = ((Activity) context).findViewById(R.id.site_user_pic);
                                            TextView siteUsername = ((Activity) context).findViewById(R.id.site_username);

                                            Picasso.get().load(user.profile_pic).into(siteUserPic);
                                            siteUsername.setText(user.username);

                                            siteUserLogged.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                }
                                            });
                                        } else {
                                            loginBtns.setVisibility(View.VISIBLE);
                                            userLogged.setVisibility(View.GONE);
                                        }
                                        break;
                                    case 404:
                                        // User not found display it on the screen
                                        siteLoginErrorText.setText("Nie znaleziono takiego użytkownika");
                                        break;
                                    case 401:
                                        // Invalid login data
                                        siteLoginErrorText.setText("Podano nieprawidłowe dane logowania");
                                        break;
                                    default:
                                        // Technical problems with database
                                        siteLoginErrorText.setText("Wystąpił problem techniczny z bazą danych");
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

            requestQueue.add(userReq);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }
    }
}
