package com.example.pollingpal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pollingpal.Pages.MainPage;

public class MainActivity extends AppCompatActivity {
    public String currentPage;
    public String API = "http://192.168.1.42:8000";
    public MainPage mainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentPage = "mainPage";

        mainPage = new MainPage(this);
        mainPage.fetchPolls();
    }
}