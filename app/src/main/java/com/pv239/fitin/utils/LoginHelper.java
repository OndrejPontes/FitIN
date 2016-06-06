package com.pv239.fitin.utils;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by opontes on 06/06/16.
 */
public class LoginHelper {
    public static void login(final Firebase ref, String emailVal, String passVal, final LoginCallBack callback) {
        ref.authWithPassword(emailVal, passVal, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(final AuthData authData) {
                if (callback != null) {
                    callback.callbackFunction();
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.i(Constants.TAG, "Authenticating error");
            }
        });
    }

    public static void register(final Firebase ref, String emailVal, String passVal, final String nameVal) {
        ref.authWithPassword(emailVal, passVal, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(final AuthData authData) {
                ref.child("users").child(authData.getUid()).child("name").setValue(nameVal);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.i(Constants.TAG, "Authenticating error");
            }
        });
    }

    public interface LoginCallBack {
        void callbackFunction();
    }
}