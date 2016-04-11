package com.pv239.fitin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        String newString = null;
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString = null;
            } else {
                newString = extras.getString("uid");
            }
        }
        Log.i(Constants.TAG, "Logged in activity");
        Log.i(Constants.TAG, newString);

        final Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
            }
        });
    }

    public void onLogout() {
        //TODO: Log the user out.
        /*Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.unauth();*/

        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(Constants.LOGOUT);
        sendBroadcast(broadCastIntent);

        finish();
    }
}
