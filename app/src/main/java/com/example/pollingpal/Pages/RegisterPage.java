package com.example.pollingpal.Pages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterPage extends MainActivity {
    public Context context;
    public LoginPage loginPage;
    public MainPage mainPage;

    public RegisterPage(Context context) {
        this.context = context;

        View mainPageView = ((Activity) context).findViewById(R.id.site_main_page);
        View addPollPageView = ((Activity) context).findViewById(R.id.site_add_poll_page);
        View loginPageView = ((Activity) context).findViewById(R.id.site_login_page);
        View registerPageView = ((Activity) context).findViewById(R.id.site_register_page);

        loginPageView.setVisibility(View.GONE);
        mainPageView.setVisibility(View.GONE);
        addPollPageView.setVisibility(View.GONE);
        registerPageView.setVisibility(View.VISIBLE);

        Button siteUserRegister = ((Activity) context).findViewById(R.id.site_user_register);
        TextView siteUserLogin = ((Activity) context).findViewById(R.id.site_user_login_register_page);

        TextView siteReturnToMainPage = ((Activity) context).findViewById(R.id.site_register_return_to_main_page);

        siteReturnToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPage = new MainPage(context, null);
            }
        });

        siteUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLoginPage();
            }
        });

        siteUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    public void registerUser() {
        EditText uniqueId = ((Activity) context).findViewById(R.id.site_register_login_input);
        EditText username = ((Activity) context).findViewById(R.id.site_register_username_input);
        EditText pass = ((Activity) context).findViewById(R.id.site_register_pass_input);
        EditText confirmPass = ((Activity) context).findViewById(R.id.site_register_confirm_pass_input);
        EditText email = ((Activity) context).findViewById(R.id.site_register_email_input);

        String uniqueIdText = uniqueId.getText().toString();
        String usernameText = username.getText().toString();
        String passText = pass.getText().toString();
        String confirmPassText = confirmPass.getText().toString();
        String emailText = email.getText().toString();

        if (uniqueIdText.isEmpty()
                || passText.isEmpty()
                || confirmPassText.isEmpty()
                || emailText.isEmpty()
                || usernameText.isEmpty()
                || !passText.equals(confirmPassText)
                || !emailText.contains("@")
                || !emailText.contains(".")
        ) return;

        JSONObject registerPayload = new JSONObject();

        try {
            registerPayload.put("uniqueId", uniqueIdText);
            registerPayload.put("username", usernameText);
            registerPayload.put("pass", passText);
            registerPayload.put("email", emailText);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException", e.toString());
        }

        Log.d("registerPayload", registerPayload.toString());

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            String requestURL = API + "/users/register";

            JsonObjectRequest registerReq = new JsonObjectRequest(Request.Method.POST, requestURL, registerPayload,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {}
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.d("error", error.toString());
                        }
                    }
            );

            requestQueue.add(registerReq);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", e.toString());
        }
    }

    public void switchToLoginPage() {
        loginPage = new LoginPage(context);
    }
}
