package com.pv239.fitin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pv239.fitin.auth.LoginFragment;
import com.pv239.fitin.auth.RegisterFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener, RegisterFragment.OnRegisterListener {

    Context context = this;
    Button button;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startActivity(new Intent(MainActivity.this, TestRecyclerViewActivity.class));

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constants.LOGOUT);
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d(Constants.TAG, "logout in progress");
//
//                onLogout();
//            }
//        }, intentFilter);

        if (savedInstanceState == null) {
            Firebase.setAndroidContext(this);
        }

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        if (firebase.getAuth() == null || isExpired(firebase.getAuth())) {
//            Intent intent = new Intent(context, LoggedInActivity.class);
//            intent.putExtra("uid", firebase.getAuth().getUid());
//            startActivity(intent);
            switchToLoginFragment();
        }
        // User is logged in

        button = (Button) findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(Constants.TAG, "logout button clicked");
                onLogout();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean isExpired(AuthData authData) {
        return (System.currentTimeMillis() / 1000) >= authData.getExpires();
    }

    @Override
    public void onLogin(String email, String password) {
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.authWithPassword(email, password, new MyAuthResultHandler());
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pv239.fitin/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pv239.fitin/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class MyAuthResultHandler implements Firebase.AuthResultHandler {
        @Override
        public void onAuthenticated(AuthData authData) {
//            Intent intent = new Intent(context, LoggedInActivity.class);
//            intent.putExtra("uid", authData.getUid());
//            startActivity(intent);
            removeLoginFragment();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
//            showLoginError("onAuthenticationError: " + firebaseError.getMessage());
            Log.i(Constants.TAG, "onAuthenticationError: " + firebaseError.getMessage());
        }
    }

    @Override
    public void onGoogleLogin() {

    }

    @Override
    public void onRegister() {
        switchToRegisterFragment();
    }

    public void onLogout() {
        //TODO: Log the user out.
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.unauth();
        switchToLoginFragment();
    }

    //Register fragment
    private class RegisterResultHandler implements Firebase.ValueResultHandler {

        @Override
        public void onSuccess(Object o) {
            //switchToLoginFragment();
        }

        @Override
        public void onError(FirebaseError firebaseError) {

        }
    }

    @Override
    public void onRegister(String email, String password) {
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.createUser(email, password, new RegisterResultHandler());
        onLogin(email, password);
    }

    @Override
    public void onBackToLogin() {

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

    private void switchToRegisterFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_activity, new RegisterFragment(), "Register");
        ft.commit();
    }

    public void removeFragmentByTag(String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.remove(getSupportFragmentManager().findFragmentById(R.id.main_activity));
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(tag);
        ft.remove(loginFragment);
        ft.commit();
    }

    private void showLoginError(String message) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("Login");
        loginFragment.onLoginError(message);
    }
}
