package com.pv239.fitin.Entities;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.pv239.fitin.utils.DownloadImageTask;
import com.pv239.fitin.utils.DownloadImageTaskBackground;
import com.pv239.fitin.utils.Provider;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class User {
    private Provider provider;

    private TextView nameView;
    private TextView emailView;
    private View coverImageView;
    private CircleImageView profileImageView;

    private String name;
    private String email;
    private String coverImageUrl;
    private String profileImageUrl;

    public User(Provider provider) {
        this.provider = provider;
    }

    public void setName(String name, TextView nameView) {
        this.name = name;
        this.nameView = nameView;
        this.nameView.setText(this.name);
    }

    public void setEmail(String email, TextView emailView) {
        this.email = email;
        this.emailView = emailView;
        this.emailView.setText(this.email);
    }

    public void setCoverImage(String coverImageUrl, View coverImageView) {
        this.coverImageUrl = coverImageUrl;
        this.coverImageView = coverImageView;
        new DownloadImageTaskBackground(this.coverImageView)
                .execute(coverImageUrl);
    }

    public void setProfileImage(String profileImageUrl, CircleImageView profileImageView, Context context) {
        this.profileImageUrl = profileImageUrl;
        this.profileImageView = profileImageView;
        Picasso.with(context)
                .load(profileImageUrl)
                .into(profileImageView);
//        new DownloadImageTask(this.profileImageView)
//                .execute(profileImageUrl);
    }

    public Provider getProvider() {
        return this.provider;
    }
}
