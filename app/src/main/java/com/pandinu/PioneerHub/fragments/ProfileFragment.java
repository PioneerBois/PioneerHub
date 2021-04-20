package com.pandinu.PioneerHub.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pandinu.PioneerHub.R;

public class ProfileFragment extends Fragment {
    private ImageButton ibSetting, ibEditInfo;
    private ImageView ivCoverImage, ivProfileImage;
    private TextView tvUserFullName, tvUserInfo, tvUserDepartment;
    private RecyclerView rvUserPosts;

    public static final String ARG_USER_FULLNAME = "USER_FULL_NAME";
    public static final String ARG_USER_INFO = "USER_INFO";
    public static final String ARG_USER_DEPARTMENT = "USER_DEPT";
    public static final String ARG_PROFILE_IMG_URL = "USER_PROFILE_IMAGE_URL";
    public static final String ARG_COVER_IMAGE_URL = "USER_COVER_IMAGE_URL";


    public ProfileFragment() {
        // Required empty public constructor
        //setRetainInstance(true);
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String usrFullName, String usrInfo, String usrDepartment, String usrProfileImgUrl, String usrCoverImgUrl) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        args.putString(ARG_USER_FULLNAME, usrFullName);
        args.putString(ARG_USER_INFO, usrInfo);
        args.putString(ARG_USER_DEPARTMENT, usrDepartment);
        args.putString(ARG_PROFILE_IMG_URL, usrProfileImgUrl);
        args.putString(ARG_COVER_IMAGE_URL, usrCoverImgUrl);

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
        Glide.with(getContext())
                .load(getArguments().getString(ARG_COVER_IMAGE_URL))
                .into(ivCoverImage);

        ivProfileImage= (ImageView)v.findViewById(R.id.iv_profile_image);
        Glide.with(getContext())
                .load(getArguments().getString(ARG_PROFILE_IMG_URL))
                .placeholder(R.drawable.ic_baseline_person_24)
                .override(300, 300)
                .centerCrop()
                .into(ivProfileImage);

        tvUserFullName= (TextView)v.findViewById(R.id.tv_full_name);
        tvUserFullName.setText(getArguments().getString(ARG_USER_FULLNAME));

        tvUserInfo= (TextView)v.findViewById(R.id.tv_info_text);
        tvUserInfo.setText(getArguments().getString(ARG_USER_INFO));

        tvUserDepartment= (TextView)v.findViewById(R.id.tv_department);
        tvUserDepartment.setText(getArguments().getString(ARG_USER_DEPARTMENT));

        rvUserPosts = (RecyclerView)v.findViewById(R.id.rv_user_posts);



        // Inflate the layout for this fragment
        return v;
    }
}