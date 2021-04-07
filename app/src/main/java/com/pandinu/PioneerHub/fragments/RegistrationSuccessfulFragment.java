package com.pandinu.PioneerHub.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pandinu.PioneerHub.R;

public class RegistrationSuccessfulFragment extends Fragment {


    private RegistrationSuccessfulListener listener;
    private Button btnConfirm;

    public interface RegistrationSuccessfulListener{
        void confirmSuccessfulRegistration();
    }


    public RegistrationSuccessfulFragment() {
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
        View v = inflater.inflate(R.layout.fragment_registration_successful, container, false);

        btnConfirm = v.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirmSuccessfulRegistration();
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RegistrationSuccessfulListener){
            listener = (RegistrationSuccessfulListener) context;
        }else{
            throw new RuntimeException(context.toString() + " " +
                    "RegistrationSuccessfulListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}