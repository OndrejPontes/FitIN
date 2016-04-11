package com.pv239.fitin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.pv239.fitin.auth.LoginFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startActivity(new Intent(MainActivity.this, TestRecyclerViewActivity.class));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGOUT);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(Constants.TAG, "logout in progress");

                onLogout();
            }
        }, intentFilter);

        if(savedInstanceState == null) {
            Firebase.setAndroidContext(this);
        }

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        if(firebase.getAuth() != null && !isExpired(firebase.getAuth())) {
            //User is logged in!
            Intent intent = new Intent(context, LoggedInActivity.class);
            intent.putExtra("uid", firebase.getAuth().getUid());
            startActivity(intent);
        } else {
            switchToLoginFragment();
        }

    }

    private boolean isExpired(AuthData authData) {
        return (System.currentTimeMillis() / 1000) >= authData.getExpires();
    }

    @Override
    public void onLogin(String email, String password) {
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.authWithPassword(email, password, new MyAuthResultHandler());
    }

    private class MyAuthResultHandler implements Firebase.AuthResultHandler {
        @Override
        public void onAuthenticated(AuthData authData) {
            Intent intent = new Intent(context, LoggedInActivity.class);
            intent.putExtra("uid", authData.getUid());
            startActivity(intent);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            showLoginError("onAuthenticationError: " + firebaseError.getMessage());
        }
    }

    @Override
    public void onGoogleLogin() {

    }

    public void onLogout() {
        //TODO: Log the user out.
        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        firebase.unauth();
    }

    private void switchToLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, new LoginFragment(), "Login");
        ft.commit();
    }

    private void showLoginError(String message) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("Login");
        loginFragment.onLoginError(message);
    }
}
