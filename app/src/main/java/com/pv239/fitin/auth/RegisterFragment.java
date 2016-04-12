package com.pv239.fitin.auth;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.pv239.fitin.R;

public class RegisterFragment extends Fragment {
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordAgainView;
    private View mRegisterForm;
    private View mProgressSpinner;
    private boolean mRegistering;
    private OnRegisterListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegistering = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        mEmailView = (EditText) rootView.findViewById(R.id.register_email);
        mPasswordView = (EditText) rootView.findViewById(R.id.register_password);
        mPasswordAgainView = (EditText) rootView.findViewById(R.id.register_password_again);
        mRegisterForm = rootView.findViewById(R.id.register_form);
        mProgressSpinner = rootView.findViewById(R.id.register_progress);
        View registerButton = rootView.findViewById(R.id.submit_register_button);
        View backToLoginButton = rootView.findViewById(R.id.register_back_button);
//        mGoogleSignInButton = (SignInButton) rootView.findViewById(R.id.google_sign_in_button);
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_NEXT) {
                    mPasswordView.requestFocus();
                    return true;
                }
                return false;
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    register();
                    return true;
                }
                return false;
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //backToLogin();
            }
        });

        return rootView;
    }

    public void register() {
        if (mRegistering) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passwordAgain = mPasswordAgainView.getText().toString();

        boolean cancelRegister = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password, passwordAgain)) {
            mPasswordView.setError(getString(R.string.invalid_password));
            focusView = mPasswordView;
            cancelRegister = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.field_required));
            focusView = mEmailView;
            cancelRegister = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.invalid_email));
            focusView = mEmailView;
            cancelRegister = true;
        }

        if (cancelRegister) {
            // error in register
            focusView.requestFocus();
        } else {
            // show progress spinner, and start background task to login
            showProgress(true);
            mRegistering = true;
            mListener.onRegister(email, password);
            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
    }

    public void onLoginError(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.login_error))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();

        showProgress(false);
        mRegistering = false;
    }

    private void showProgress(boolean show) {
        mProgressSpinner.setVisibility(show ? View.VISIBLE : View.GONE);
        mRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mListener = (OnRegisterListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password, String passwordAgain) {
        return password.equals(passwordAgain) && password.length() > 4;
    }

    public interface OnRegisterListener {
        // TODO: Update argument type and name
        void onRegister(String email, String password);

        void onBackToLogin();
    }
}
