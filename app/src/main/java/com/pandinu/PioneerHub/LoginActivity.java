package com.pandinu.PioneerHub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.pandinu.PioneerHub.fragments.LoginFragment;
import com.pandinu.PioneerHub.fragments.RegisterFragment;
import com.pandinu.PioneerHub.fragments.RegistrationSuccessfulFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener, RegistrationSuccessfulFragment.RegistrationSuccessfulListener {



    //private RegisterFragment registerFragment;
    //private LoginFragment loginFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //registerFragment = new RegisterFragment();

        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, loginFragment);
        ft.commit();

    }

    @Override
    public void loginSuccessful() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void register() {


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new RegisterFragment()).addToBackStack(null);
        ft.commit();
    }

    @Override
    public void goBackToLogin() {
        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.replace(R.id.container, loginFragment);
        ft.commit();*/
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void confirmRegistration() {
        getSupportFragmentManager().popBackStack();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new RegistrationSuccessfulFragment()).addToBackStack(null);
        ft.commit();
    }

    @Override
    public void confirmSuccessfulRegistration() {
        goBackToLogin();
    }


    //Go back to the previous fragment

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        Log.i("count", String.valueOf(count));
        if (count == 0) {
            super.onBackPressed();

            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }


}