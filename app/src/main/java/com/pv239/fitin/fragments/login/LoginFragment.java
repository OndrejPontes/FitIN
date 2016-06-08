package com.pv239.fitin.fragments.login;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.pv239.fitin.MainActivity;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;

public class LoginFragment extends Fragment {

    Firebase ref;

    private RelativeLayout firebaseLoginLayout;

    private LinearLayout loginProgressLayout;

    public void setRef(Firebase ref) {
        this.ref = ref;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        loginProgressLayout = (LinearLayout) rootView.findViewById(R.id.login_progress_layout);
        firebaseLoginLayout = (RelativeLayout) rootView.findViewById(R.id.firebase_login_layout);
        hideProgressBar();

        final EditText email = (EditText) rootView.findViewById(R.id.login_email);
        final EditText pass = (EditText) rootView.findViewById(R.id.login_password);
        Button loginButton = (Button) rootView.findViewById(R.id.login_button);
        Button registerButton = (Button) rootView.findViewById(R.id.go_to_register_button);

        final MainActivity mainActivity = (MainActivity) getActivity();

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
                InputMethodManager imm = (InputMethodManager)mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
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
        loginProgressLayout.setVisibility(View.VISIBLE);
        firebaseLoginLayout.setVisibility(View.GONE);
    }
    public void hideProgressBar() {
        loginProgressLayout.setVisibility(View.GONE);
        firebaseLoginLayout.setVisibility(View.VISIBLE);
    }

    private void updateDisplay(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_layout, fragment).commit();
    }
}