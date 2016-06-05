package com.pv239.fitin.fragments.login;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;

public class LoginFragment extends Fragment {

    Firebase ref;

    private RelativeLayout firebaseLoginLayout;

    private ProgressBar loginProgressBar;

    public void setRef(Firebase ref) {
        this.ref = ref;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        loginProgressBar = (ProgressBar) rootView.findViewById(R.id.login_progress_bar);
        firebaseLoginLayout = (RelativeLayout) rootView.findViewById(R.id.firebase_login_layout);
        hideProgressBar();

        final EditText email = (EditText) rootView.findViewById(R.id.login_email);
        final EditText pass = (EditText) rootView.findViewById(R.id.login_password);
        Button loginButton = (Button) rootView.findViewById(R.id.login_button);
        Button registerButton = (Button) rootView.findViewById(R.id.go_to_register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String emailVal = email.getText().toString();
            String passVal = pass.getText().toString();
            Log.i(Constants.TAG, "Login " + emailVal + " " + passVal);
            showProgressBar();
            ref.authWithPassword(emailVal, passVal, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    hideProgressBar();
                    Log.i(Constants.TAG, "Authenticated");
                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    hideProgressBar();
                    Log.i(Constants.TAG, "Authenticating error");
                }
            });
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment fragment = new RegisterFragment();
                fragment.setRef(ref);
                updateDisplay(fragment);
            }
        });

        return rootView;
    }

    public void logout() {
        ref.unauth();
    }

    public void showProgressBar() {
        loginProgressBar.setVisibility(View.VISIBLE);
        firebaseLoginLayout.setVisibility(View.GONE);
    }
    public void hideProgressBar() {
        loginProgressBar.setVisibility(View.GONE);
        firebaseLoginLayout.setVisibility(View.VISIBLE);
    }

    private void updateDisplay(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_layout, fragment).commit();
    }
}