package com.pv239.fitin.Entities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
}
