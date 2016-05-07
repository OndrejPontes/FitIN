package com.pv239.fitin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.pv239.fitin.auth.LoginFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener, /*RegisterFragment.OnRegisterListener,*/ GoogleApiClient.OnConnectionFailedListener {

    Context context = this;
    Button logoutButton;
    TextView welcomeUser;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(this.getString(R.string.server_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        switchToLoginFragment();

        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(Constants.TAG, "logout button clicked");
                onLogout();
            }
        });

        welcomeUser = (TextView) findViewById(R.id.welcomeText);
    }


    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(Constants.TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleGoogleSignInResult(result);
        } else {
            Log.i(Constants.TAG, "No cached sign in");
            switchToLoginFragment();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(Constants.TAG, "handleSignInResult:" + result.isSuccess());
        if(result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            welcomeUser.setText(getString(R.string.welcome_user_text, acct.getDisplayName()));
            TextView infoText = (TextView) findViewById(R.id.info_text);
            infoText.setText("Id: " + acct.getId() + " email:" + acct.getEmail()
                    + " photoUrl: " + acct.getPhotoUrl());
            if(acct.getPhotoUrl() != null) {
                Log.i(Constants.TAG, acct.getPhotoUrl().toString());
            }
            removeLoginFragment();
        }
    }

    @Override
    public void onGoogleLogin() {
        Intent singnInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(singnInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result =Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }

    public void onLogout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        switchToLoginFragment();
                    }
                });
    }


    private void switchToLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity, new LoginFragment(), "Login");
        ft.commit();
    }

    public void removeLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.remove(getSupportFragmentManager().findFragmentById(R.id.main_activity));
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("Login");
        ft.remove(loginFragment);
        ft.commit();
    }

//    private void switchToRegisterFragment() {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.main_activity, new RegisterFragment(), "Register");
//        ft.commit();
//    }
//
//    public void removeFragmentByTag(String tag) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        //ft.remove(getSupportFragmentManager().findFragmentById(R.id.main_activity));
//        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(tag);
//        ft.remove(loginFragment);
//        ft.commit();
//    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(Constants.TAG, "onConnectionFailed:" + connectionResult);
    }
}
