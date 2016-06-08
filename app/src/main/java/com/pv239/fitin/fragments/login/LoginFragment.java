package com.pv239.fitin.fragments.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.pv239.fitin.MainActivity;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;

import java.io.IOException;

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    Firebase ref;

    private RelativeLayout firebaseLoginLayout;

    private LinearLayout loginProgressLayout;

    private GoogleApiClient mGoogleApiClient;
    private SignInButton mGoogleSignInButton;
    private static final int REQUEST_CODE_GOOGLE_LOGIN = 9001;

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
        mGoogleSignInButton = (SignInButton) rootView.findViewById(R.id.google_sign_in_button);
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

        //Google

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        if(mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient
                .Builder(mainActivity)
                .enableAutoManage(mainActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithGoogle();
            }
        });

        return rootView;
    }

    //Google
    private void loginWithGoogle() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, REQUEST_CODE_GOOGLE_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GOOGLE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                String emailAddress = account.getEmail();
                getGoogleOAuthToken(emailAddress);
            } else {
                Status status = result.getStatus();
                status.getStatus();
            }
        }
    }
    private void getGoogleOAuthToken(final String emailAddress) {
        final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    String scope = "oauth2:profile email";
                    token = GoogleAuthUtil.getToken(getContext(), emailAddress, scope);
                } catch (IOException transientEx) {
                /* Network or server error */
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                /* We probably need to ask for permissions, so start the intent if there is none pending */
                    Intent recover = e.getIntent();
                    startActivityForResult(recover, REQUEST_CODE_GOOGLE_LOGIN);
                } catch (GoogleAuthException authEx) {
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }
                return token;
            }
            @Override
            protected void onPostExecute(String token) {
                Log.d(Constants.TAG, "onPostExecute, token: " + token);
                if(token != null) {
                    onGoogleLoginWithToken(token);
                } {
                    String err = errorMessage==null ? "null" : errorMessage;
                    Log.i(Constants.TAG, err);
                }
            }
        };
        task.execute();
    }

    private void onGoogleLoginWithToken(String oAuthToken) {
        ref.authWithOAuthToken("google", oAuthToken, null);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(Constants.TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }
}