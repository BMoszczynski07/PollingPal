package com.example.pollingpal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.pollingpal.Models.User;
import com.example.pollingpal.Pages.LoginPage;
import com.example.pollingpal.Pages.MainPage;
import com.example.pollingpal.Pages.UserInformation;

public class MainActivity extends AppCompatActivity {
    public String API = "http://192.168.122.104:8000";
    public MainPage mainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPage = new MainPage(this, null);
    }
}