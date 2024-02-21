package com.example.pollingpal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pollingpal.Models.User;
import com.example.pollingpal.Pages.LoginPage;
import com.example.pollingpal.Pages.MainPage;

public class MainActivity extends AppCompatActivity {
    public String API = "http://192.168.1.42:8000";
    public MainPage mainPage;
    public LoginPage loginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPage = new MainPage(this, null);
    }
}