package com.pv239.fitin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Admin on 14.05.2016.
 */
public class DownloadImageTaskBackground extends AsyncTask<String, Void, Bitmap> {
    View bmImage;

    public DownloadImageTaskBackground(View bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        InputStream in;
        try {
            if(urldisplay == null) {
                in = new FileInputStream("drawable/cover_pic.jpg");
                mIcon11 = BitmapFactory.decodeStream(in);
            } else {
                in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setBackground(new BitmapDrawable(result));
    }
}