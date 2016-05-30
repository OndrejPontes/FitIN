package com.pv239.fitin.Entities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.DownloadImageTask;
import com.pv239.fitin.utils.DownloadImageTaskBackground;
import com.pv239.fitin.utils.Provider;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;

public class User {

    @JsonIgnore
    private String id;

    private Provider provider;


    private TextView nameView;
    private TextView emailView;
    private View coverImageView;
    private CircleImageView profileImageView;

    private String name;
    private String email;
    private String coverImageUrl;
    private String profileImageUrl;

    public User() {
    }

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setCoverImage(String coverImageUrl, View coverImageView, Context context) {
        this.coverImageUrl = coverImageUrl;
        this.coverImageView = coverImageView;
        if(coverImageUrl == null) {
            coverImageView.setBackground(context.getDrawable(R.drawable.cover_pic));
        } else {
            new DownloadImageTaskBackground(this.coverImageView)
                    .execute(coverImageUrl);
        }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public TextView getNameView() {
        return nameView;
    }

    public void setNameView(TextView nameView) {
        this.nameView = nameView;
    }

    public TextView getEmailView() {
        return emailView;
    }

    public void setEmailView(TextView emailView) {
        this.emailView = emailView;
    }

    public View getCoverImageView() {
        return coverImageView;
    }

    public void setCoverImageView(View coverImageView) {
        this.coverImageView = coverImageView;
    }

    public CircleImageView getProfileImageView() {
        return profileImageView;
    }

    public void setProfileImageView(CircleImageView profileImageView) {
        this.profileImageView = profileImageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
