package com.example.pollingpal.Pages;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pollingpal.MainActivity;
import com.example.pollingpal.Models.User;
import com.example.pollingpal.R;
import com.squareup.picasso.Picasso;

public class UserInformation extends MainActivity {
    public Context context;
    public User user;

    public UserInformation(Context context, User user) {
        this.context = context;
        this.user = user;

        View mainPageView = ((Activity) context).findViewById(R.id.site_main_page);
        View loginPage = ((Activity) context).findViewById(R.id.site_login_page);
        View addPollPage = ((Activity) context).findViewById(R.id.site_add_poll_page);
        View userInfo = ((Activity) context).findViewById(R.id.user);

        loginPage.setVisibility(View.GONE);
        mainPageView.setVisibility(View.GONE);
        addPollPage.setVisibility(View.GONE);
        userInfo.setVisibility(View.VISIBLE);

        Button logout = ((Activity) context).findViewById(R.id.user_logout);
        TextView backToMainPage = ((Activity) context).findViewById(R.id.user_back_to_main_page);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPage = new MainPage(context, null);

                View userLogged = ((Activity) context).findViewById(R.id.site_user_logged);
                View loginBtns = ((Activity) context).findViewById(R.id.site_login_btns);

                loginBtns.setVisibility(View.VISIBLE);
                userLogged.setVisibility(View.GONE);
            }
        });

        backToMainPage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainPage = new MainPage(context, user);
                    }
                }
        );

        appendUserInformation();
    }

    public void appendUserInformation() {
        ImageView userPic = ((Activity) context).findViewById(R.id.user_pic);
        TextView userName = ((Activity) context).findViewById(R.id.user_username);
        TextView userUniqueId = ((Activity) context).findViewById(R.id.user_unique_id);
        TextView userEmail = ((Activity) context).findViewById(R.id.user_email);
        TextView userCreateDate = ((Activity) context).findViewById(R.id.user_create_date);

        String guestProfilePATH = API + "/images/guest.png";

        Picasso.get().load(guestProfilePATH).into(userPic);
        userName.setText(user.username);
        userUniqueId.setText("@" + user.unique_id);
        userEmail.setText("email: " + user.email);
        userCreateDate.setText("Konto utworzone " + user.create_date);
    }
}
