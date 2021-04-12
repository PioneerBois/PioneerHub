package com.pandinu.PioneerHub.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pandinu.PioneerHub.Post;
import com.pandinu.PioneerHub.Profile;
import com.pandinu.PioneerHub.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;


public class PostFragment extends Fragment {
    private static final String TAG = "PostFragment";

    /*private static final String[] CHANNELS = new String[]{
      "Student Feed", "Buy & Sell", "Lost & Found", "Housing", "News", "Ride Sharing"
    };*/

    private PostFragmentListener listener;

    public interface PostFragmentListener{
        void cancelPost(String channel);
        void successfulPost(String channel);
    }

    private Button buttonCancelPost;
    private Button btnComposePost;
    private Button btnResetPost;
    private EditText etPostDescription;
    private RelativeLayout rlImage;
    private Button btnTakePhoto;
    private Button btnChoosePhoto;
    private Button btnCancelImage;
    private static final String ARG_CHANNEL = "";
    private String currentChannel = "";
    private TextView tvDescription;

    private ImageView ivProfileImage;
    private TextView tvFirstName;
    private TextView tvLastName;
    public static final int MAX_TWEET_LENGTH = 280;
    private TextView tvPostCounter;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static PostFragment newInstance(String channel) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putString(ARG_CHANNEL, channel);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        if(getArguments() != null){
            currentChannel = getArguments().getString(ARG_CHANNEL);
        }

        tvDescription = v.findViewById(R.id.tvDescription);
        tvDescription.setText(currentChannel);

        Log.i("currentChannel", currentChannel);

        ivProfileImage = v.findViewById(R.id.ivProfileImage);
        tvFirstName = v.findViewById(R.id.tvFirstName);
        tvLastName = v.findViewById(R.id.tvLastName);

        queryProfile();


        buttonCancelPost = v.findViewById(R.id.buttonCancelPost);

        buttonCancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancelPost(currentChannel);
            }
        });



        rlImage = v.findViewById(R.id.rlImage);
        etPostDescription = v.findViewById(R.id.etPostDescription);
        tvPostCounter = v.findViewById(R.id.tvPostCounter);
        etPostDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Fires right as the text is being changed (even supplies the range of text)

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Fires right before text is changing

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Fires right after the text has changed

                int tweetCharLeft = MAX_TWEET_LENGTH - editable.toString().length();

                tvPostCounter.setText("" + tweetCharLeft);
                if(tweetCharLeft <= 20){
                    //btnComposePost.setEnabled(false);
                    tvPostCounter.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));

                    //tvCharLimit.setTextColor(Color.parseColor("#FF0000"));
                }else{
                    tvPostCounter.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryVariant));
                    //btnComposePost.setEnabled(true);
                    //tvCharLimit.setTextColor(Color.parseColor("#000000"));
                }
            }
        });
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

        btnComposePost = v.findViewById(R.id.btnComposePost);
        btnComposePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = etPostDescription.getText().toString().trim();
                ParseUser currentUser = ParseUser.getCurrentUser();
                File photoFile = null;


                savePost(description, currentUser, photoFile);
                listener.successfulPost(currentChannel);
            }
        });


        return v;
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setUserId(currentUser);
        if(!(photoFile == null)){
            post.setImg(new ParseFile(photoFile));
        }
        post.setChannel(currentChannel);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error while saving", e);
                    return;
                }

                Log.i(TAG, "Post save was successful!");
            }
        });

    }

    private void queryProfile() {
        ParseQuery<Profile> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("userId", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> objects, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with querying Profile");
                    return;
                }else{
                    //Log.i(TAG, objects.get(0).getFirstName());
                    tvFirstName.setText(objects.get(0).getFirstName());
                    tvLastName.setText(objects.get(0).getLastName());
                    Glide.with(getActivity()).load(objects.get(0).getProfileImg().getUrl()).into(ivProfileImage);
                }
            }
        });

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

        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }
}