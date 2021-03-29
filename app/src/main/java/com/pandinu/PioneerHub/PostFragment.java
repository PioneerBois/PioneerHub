package com.pandinu.PioneerHub;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


public class PostFragment extends Fragment {

    /*private static final String[] CHANNELS = new String[]{
      "Student Feed", "Buy & Sell", "Lost & Found", "Housing", "News", "Ride Sharing"
    };*/

    private PostFragmentListener listener;

    public interface PostFragmentListener{
        void cancelPost();
        void successfulPost();
    }



    private AutoCompleteTextView actvChannels;
    private Button buttonCancelPost;
    private Button btnComposePost;
    private Button btnResetPost;
    private EditText etPostDescription;
    private RelativeLayout rlImage;
    private Button btnTakePhoto;
    private Button btnChoosePhoto;
    private Button btnCancelImage;



    public PostFragment() {
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
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        String[] channels = getResources().getStringArray(R.array.channels);
        actvChannels = v.findViewById(R.id.actvChannels);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, channels);
        actvChannels.setAdapter(adapter);
        actvChannels.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                actvChannels.showDropDown();
                return false;
            }
        });

        buttonCancelPost = v.findViewById(R.id.buttonCancelPost);

        buttonCancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancelPost();
            }
        });

        btnComposePost = v.findViewById(R.id.btnComposePost);
        btnComposePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.successfulPost();
            }
        });

        rlImage = v.findViewById(R.id.rlImage);
        etPostDescription = v.findViewById(R.id.etPostDescription);
        btnResetPost = v.findViewById(R.id.btnResetPost);
        btnResetPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPostDescription.setText("");
                rlImage.setVisibility(View.GONE);
                //etPostDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        });

        btnChoosePhoto = v.findViewById(R.id.btnChoosePhoto);
        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlImage.setVisibility(View.VISIBLE);
            }
        });


        btnTakePhoto = v.findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlImage.setVisibility(View.VISIBLE);
            }
        });

        btnCancelImage = v.findViewById(R.id.btnCancelImage);
        btnCancelImage.setOnClickListener(v1 -> rlImage.setVisibility(View.GONE));



        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof PostFragmentListener){
            listener = (PostFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " " +
                    "must implement PostFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}