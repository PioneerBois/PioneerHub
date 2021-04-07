package com.pandinu.PioneerHub.fragments;

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
import android.widget.Toast;

import com.pandinu.PioneerHub.Profile;
import com.pandinu.PioneerHub.R;
import com.pandinu.PioneerHub.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class RegisterFragment extends Fragment {

    public static final String TAG = "RegisterFragment";
    private TextView tvSignIn;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etReTypePassword;
    private Button btnRegister;


    private RegisterFragmentListener listener;

    public interface RegisterFragmentListener{
        void goBackToLogin();
        void confirmRegistration();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        etFirstName = v.findViewById(R.id.etFirstName);
        etLastName = v.findViewById(R.id.etLastName);
        etEmail = v.findViewById(R.id.etEmail);
        etPassword = v.findViewById(R.id.etPassword);
        etReTypePassword = v.findViewById(R.id.etReTypePassword);

        tvSignIn = v.findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goBackToLogin();
            }
        });

        btnRegister = v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String retypePassword = etReTypePassword.getText().toString().trim();
                registerUser(firstName, lastName, email, password, retypePassword);
            }
        });
        return v;
    }

    private void registerUser(String firstName, String lastName, String email, String password, String retypePassword) {
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || retypePassword.isEmpty()){
            registerFailDialog();
            return;
        }

        if(!password.equals(retypePassword)){
            registerFailDialog();
            return;
        }


        //Break down registration in two steps
        //Create a new user with email and password to USER TABLE -> results in creating a unique Parse User
        //Create a profile object and save in background the profile info with the newly created user
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG,"Issue with signup", e);
                    //registerFailDialog();
                    //Toast.makeText(LoginActivity.this, "Issue with signup!", Toast.LENGTH_SHORT).show();
                    return;
                }


                addProfileInfo(firstName, lastName);
                //Toast.makeText(LoginActivity.this, "Success with signup!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addProfileInfo(String firstName, String lastName) {
        Profile profile = new Profile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setUserId(ParseUser.getCurrentUser());
        profile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error while saving", e);
                    //registerFailDialog();
                }

                ParseUser.logOut();
                listener.confirmRegistration();

            }
        });


    }

    private void registerFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.registration_dialog_title_failed);
        builder.setMessage(R.string.register_dialog_message_failed);
        builder.setPositiveButton(R.string.dialog_try_again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void hideKeyBoard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof LoginFragment.LoginFragmentListener){
            listener = (RegisterFragmentListener) context;
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