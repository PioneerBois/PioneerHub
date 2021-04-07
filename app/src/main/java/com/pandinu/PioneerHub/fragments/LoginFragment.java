package com.pandinu.PioneerHub.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pandinu.PioneerHub.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private LoginFragmentListener listener;
    private Button btnLogin;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvRegister;


    public interface LoginFragmentListener{
        void loginSuccessful();
        void register();
    }

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ParseUser.getCurrentUser() != null){
            listener.loginSuccessful();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        etEmail = v.findViewById(R.id.etEmail);
        etPassword = v.findViewById(R.id.etPassword);
        btnLogin = v.findViewById(R.id.btnLogin);
        tvRegister = v.findViewById(R.id.tvRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                loginUser(email, password);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.register();
            }
        });

        return v;
    }

    private void hideKeyBoard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loginUser(String email, String password) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e!=null){
                    loginFailDialog();
                    Log.e(TAG,"Issue with login", e);

                    return;
                }

                listener.loginSuccessful();
            }
        });
    }

    private void loginFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.dialog_try_again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etEmail.setText("");
                etPassword.setText("");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginFragmentListener){
            listener = (LoginFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " " +
                    "must implement LoginFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}