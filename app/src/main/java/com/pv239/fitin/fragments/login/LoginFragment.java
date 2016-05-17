package com.pv239.fitin.fragments.login;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.Provider;

public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {


    private Fragment self;

    private OnLoginListener mListener;
    private SignInButton mGoogleSignInButton;
    private ProgressBar loginProgressBar;
    private RelativeLayout signInButtons;


    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_SIGN_IN = 9001;

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(opr.isDone()) {
            Log.d(Constants.TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleGoogleSignInResult(result);
        } else {
            Log.i(Constants.TAG, "No cached sign in");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken(this.getString(R.string.server_client_id))
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage((FragmentActivity) getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(Plus.API)
                    .build();
        }



//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        Log.i(Constants.TAG, "logged out");
//                    }
//                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        self = this;

        loginProgressBar = (ProgressBar) rootView.findViewById(R.id.login_progress_bar);
        signInButtons = (RelativeLayout) rootView.findViewById(R.id.sign_in_buttons);

//        Button loginButton = (Button) rootView.findViewById(R.id.login_button);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getFragmentManager().beginTransaction().remove(self).commit();
//            }
//        });
        mGoogleSignInButton = (SignInButton) rootView.findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });

        return rootView;
    }

    private void loginWithGoogle() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }

    public void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.i(Constants.TAG, "handle sign in result: " + result.isSuccess());
        showProgressBar();
        if(result.isSuccess()) {
            final GoogleSignInAccount acct = result.getSignInAccount();


            Plus.PeopleApi.load(mGoogleApiClient, acct.getId()).setResultCallback(new ResultCallback<People.LoadPeopleResult>() {
                @Override
                public void onResult(People.LoadPeopleResult loadPeopleResult) {
                    if (loadPeopleResult.getStatus().isSuccess()) {
                        PersonBuffer personBuffer = loadPeopleResult.getPersonBuffer();
                        if (personBuffer != null && personBuffer.getCount() > 0) {
                            Person currentUser = personBuffer.get(0);
                            personBuffer.release();
                            Person.Cover cover = currentUser.getCover();
                            if (cover != null) {
                                Person.Cover.CoverPhoto coverPhoto = cover.getCoverPhoto();
                                if (coverPhoto != null) {
                                    String userCoverPhotoUrl = coverPhoto.getUrl();
                                    Log.i(Constants.TAG, "Cover photo Url :" + userCoverPhotoUrl);
                                    mListener.onGoogleSignUp(acct, userCoverPhotoUrl);
                                } else {
                                    mListener.onGoogleSignUp(acct, null);
                                }
                            } else {
                                Log.i(Constants.TAG, "Person has no cover");
                                mListener.onGoogleSignUp(acct, null);
                            }
                        }
                    } else {
                        mListener.onGoogleSignUp(acct, null);
                    }
                }
            });



        } else {
            Log.i(Constants.TAG, result.getStatus().toString());
            hideProgressBar();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GREP", "onConnectionFailed:" + connectionResult);
    }

    public void logout(Provider provider, ResultCallback resultCallback) {
        switch (provider) {
            case Google:
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(resultCallback);
                break;
        }
        hideProgressBar();
    }

    @Override
    public void onAttach(Context activity) {
        Log.i(Constants.TAG, "on Attatch");
        super.onAttach(activity);
        try {
            mListener = (OnLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(Constants.TAG, "on Attatch activity");
        super.onAttach(activity);
        try {
            mListener = (OnLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.i(Constants.TAG, "onDetach");
        super.onDetach();
    }

    public void showProgressBar() {
        loginProgressBar.setVisibility(View.VISIBLE);
        signInButtons.setVisibility(View.GONE);
    }
    public void hideProgressBar() {
        signInButtons.setVisibility(View.VISIBLE);
        loginProgressBar.setVisibility(View.GONE);
    }

    public interface OnLoginListener {
        // Google
        void onGoogleSignUp(GoogleSignInAccount acct, String coverPhotoUrl);
    }
}