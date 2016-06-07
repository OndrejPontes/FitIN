package com.pv239.fitin.fragments.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.pv239.fitin.entities.Filter;
import com.pv239.fitin.entities.User;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    Firebase ref;
    private ProgressBar registerProgressBar;
    private RelativeLayout firebaseRegisterLayout;


    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        registerProgressBar = (ProgressBar) rootView.findViewById(R.id.register_progress_bar);
        firebaseRegisterLayout = (RelativeLayout) rootView.findViewById(R.id.firebase_register_layout);
        hideProgressBar();

        final EditText name = (EditText) rootView.findViewById(R.id.register_name);
        final EditText email = (EditText) rootView.findViewById(R.id.register_email);
        final EditText pass = (EditText) rootView.findViewById(R.id.register_password);
        Button registerButton = (Button) rootView.findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameVal = name.getText().toString();
                final String emailVal = email.getText().toString();
                final String passVal = pass.getText().toString();
                Log.i(Constants.TAG, "register button clicked " + nameVal + " " + emailVal + " " + passVal);

                ref.createUser(emailVal, passVal, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Log.i(Constants.TAG, "User registered");
                        ref.authWithPassword(emailVal, passVal, new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
//                                ref.child("users").child(authData.getUid()).child("name").setValue(nameVal);

                                User user = new User();
                                user.setName(nameVal);
                                user.setEmail(emailVal);
                                user.setId(authData.getUid());
                                user.setFavouriteGyms(new ArrayList<String>());
                                user.setFilters(new ArrayList<Filter>());

                                ref.child("users").child(authData.getUid()).setValue(user);
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Log.i(Constants.TAG, "User registration error" + firebaseError);
                    }
                });
            }
        });

        return rootView;
    }

    public void showProgressBar() {
        registerProgressBar.setVisibility(View.VISIBLE);
        firebaseRegisterLayout.setVisibility(View.GONE);
    }
    public void hideProgressBar() {
        registerProgressBar.setVisibility(View.GONE);
        firebaseRegisterLayout.setVisibility(View.VISIBLE);
    }

    private void updateDisplay(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.drawer_layout, fragment).commit();
    }
}
