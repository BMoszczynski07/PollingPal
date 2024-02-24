package com.example.pollingpal.Pages;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.pollingpal.Models.User;
import com.example.pollingpal.R;

import java.util.ArrayList;

public class AddPollPage {
    Context context;
    User user;
    int optionsCounter = 0;
    ArrayList<View> optionsEditTexts = new ArrayList<>();

    public AddPollPage(Context context, User user) {
        this.context = context;
        this.user = user;

        View mainPageView = ((Activity) context).findViewById(R.id.site_main_page);
        View loginPage = ((Activity) context).findViewById(R.id.site_login_page);
        View addPollPage = ((Activity) context).findViewById(R.id.site_add_poll_page);

        loginPage.setVisibility(View.GONE);
        mainPageView.setVisibility(View.GONE);
        addPollPage.setVisibility(View.VISIBLE);

        Button siteAddOption = ((Activity) context).findViewById(R.id.site_add_option);

        siteAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOption();
            }
        });
    }

    public void addOption() {
        if (optionsCounter == 10) return;

        optionsCounter++;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout siteEditOptionsContainer = ((Activity) context).findViewById(R.id.site_edit_options_container);

        View siteEdit = inflater.inflate(R.layout.site_edit_option, siteEditOptionsContainer, false);
        EditText editText = siteEdit.findViewById(R.id.site_edit_option);

        editText.setId(View.generateViewId());

        editText.setHint("Opcja nr " + optionsCounter);

        optionsEditTexts.add(siteEdit);

        siteEditOptionsContainer.addView(siteEdit);

        Log.d("options", optionsEditTexts.toString());
    }
}
