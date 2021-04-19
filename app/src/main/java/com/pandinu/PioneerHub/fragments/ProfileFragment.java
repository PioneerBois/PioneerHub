package com.pandinu.PioneerHub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pandinu.PioneerHub.Post;
import com.pandinu.PioneerHub.PostsAdapter;
import com.pandinu.PioneerHub.Profile;
import com.pandinu.PioneerHub.R;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProfileFragment extends Fragment {
    private ImageButton ibSetting, ibEditInfo;
    private ImageView ivCoverImage, ivProfileImage;
    private TextView tvUserFullName, tvUserInfo, tvUserDepartment;
    private RecyclerView rvUserPosts;
    private PostsAdapter adapter;

    private List<Post> userPosts;

    public static final String ARG_USER_ID = "USER_ID";

    private String firstName, lastName, profileDesc, major, profileImg, coverImg;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        args.putString(ARG_USER_ID, userId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        getUserInfo();

        ibEditInfo = (ImageButton)v.findViewById(R.id.ib_edit_info);
        ibEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Editing info. Beep boop beep", Toast.LENGTH_SHORT).show();
            }
        });
        ibSetting= (ImageButton)v.findViewById(R.id.ib_setting_button);
        ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Opening settings page. Beep boop beep", Toast.LENGTH_SHORT).show();
            }
        });
        //https://cdn.wallpaperhub.app/cloudcache/6/9/0/e/e/f/690eefe3ba1f553e0ea527f51ee407b604b681b4.jpg
        ivCoverImage=(ImageView)v.findViewById(R.id.iv_cover_image);


        ivProfileImage= (ImageView)v.findViewById(R.id.iv_profile_image);


        tvUserFullName= (TextView)v.findViewById(R.id.tv_full_name);
        tvUserFullName.setText(firstName + " " + lastName);

        tvUserInfo= (TextView)v.findViewById(R.id.tv_info_text);
        tvUserInfo.setText(profileDesc);

        tvUserDepartment= (TextView)v.findViewById(R.id.tv_department);
        tvUserDepartment.setText(major);


        userPosts = new ArrayList<Post>();
        rvUserPosts = (RecyclerView)v.findViewById(R.id.rv_user_posts);
        adapter = new PostsAdapter(getContext(), userPosts);
        rvUserPosts.setAdapter(adapter);
        rvUserPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        getPosts();

        // Inflate the layout for this fragment
        return v;
    }

    private void getUserInfo() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        ParseUser temp_user = new ParseUser();
        temp_user.setObjectId(getArguments().getString(ARG_USER_ID));

        query.whereEqualTo("userId", temp_user);
        query.findInBackground((results, e) -> {
            // do some stuff with results
            if (e == null) {
                for (ParseObject result : results) {
                    Profile profile = (Profile) result;
                    Log.i("Test", profile.getFirstName() + " " + profile.getLastName());
                    firstName = profile.getFirstName();
                    lastName = profile.getLastName();
                    profileDesc = profile.getProfileDesc();
                    major = profile.getMajor();
                    profileImg = profile.getProfileImg().getUrl();
                    coverImg = null;
                }
                updateView();
            } else {
                //Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Test","Profile not found.");
            }
        });
    }

    private void updateView(){
        tvUserFullName.setText(firstName + " " + lastName);
        tvUserInfo.setText(profileDesc);
        tvUserDepartment.setText(major);

        Glide.with(getContext())
                .load(coverImg)
                //android:src="@drawable/ic_csueb_wordmark"
                .placeholder(R.drawable.ic_csueb_wordmark)
                .into(ivCoverImage);

        Glide.with(getContext())
                .load(profileImg)
                //.placeholder(R.drawable.ic_baseline_person_300)
                .override(300, 300)
                .centerCrop()
                .into(ivProfileImage);

        getPosts();
    }

    private  void getPosts(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        ParseUser temp_user = new ParseUser();
        temp_user.setObjectId(getArguments().getString(ARG_USER_ID));

        query.orderByDescending(Post.KEY_CREATEDAT);
        query.whereEqualTo("userId", temp_user);

        query.findInBackground((results, e) -> {
            ArrayList<Post> list = new ArrayList<Post>( );
            // do some stuff with results
            if (e == null) {
                for (ParseObject result : results) {
                    Post post = (Post)result;
                    list.add(post);
                    Log.i("Post", ((Post) result).getDescription());
                }
            } else {
                //Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Test","Profile not found.");
            }

            userPosts.clear();
            userPosts.addAll(list);
            adapter.notifyDataSetChanged();

        });
    }
}