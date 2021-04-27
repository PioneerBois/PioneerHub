package com.pandinu.PioneerHub.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pandinu.PioneerHub.LoginActivity;
import com.pandinu.PioneerHub.Post;
import com.pandinu.PioneerHub.PostsAdapter;
import com.pandinu.PioneerHub.Profile;
import com.pandinu.PioneerHub.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {


    private ImageButton ibSetting, ibEditInfo, ibSendEmail;
    private ImageView ivCoverImage, ivProfileImage;
    private TextView tvUserFullName, tvUserInfo, tvUserDepartment, tvGradDate;
    private RecyclerView rvUserPosts;
    private PostsAdapter adapter;

    private ArrayList<Object> userPosts;

    public static final String ARG_USER_ID = "USER_ID";

    private String firstName, lastName, profileDesc, major, profileImg, coverImg, gradYear, gradMonth;

    public ProfileFragment() {
        // Required empty public constructor
        //setRetainInstance(true);
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

        tvGradDate = (TextView)v.findViewById(R.id.tv_grad_date);
        tvGradDate.setText("MM, YYYY");

        ibSendEmail = (ImageButton)v.findViewById(R.id.ib_sendEmail);
        ibSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ibEditInfo = (ImageButton)v.findViewById(R.id.ib_edit_info);
        if(ParseUser.getCurrentUser().getObjectId() != getArguments().getString(ARG_USER_ID)){
            ibEditInfo.setVisibility(View.GONE);
        }
        ibEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Editing info. Beep boop beep", Toast.LENGTH_SHORT).show();
                EditProfileDialog editProfileDialog = new EditProfileDialog(ParseUser.getCurrentUser().getObjectId().toString());
                editProfileDialog.show(getActivity().getSupportFragmentManager(), "Example dialog");
            }
        });
        ibSetting= (ImageButton)v.findViewById(R.id.ib_setting_button);
        ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Opening settings page. Beep boop beep", Toast.LENGTH_SHORT).show();
                showPopup(v);
            }
        });
        //https://cdn.wallpaperhub.app/cloudcache/6/9/0/e/e/f/690eefe3ba1f553e0ea527f51ee407b604b681b4.jpg
        ivCoverImage=(ImageView)v.findViewById(R.id.iv_cover_image);


        ivProfileImage= (ImageView)v.findViewById(R.id.iv_profile_image);
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhotoPopup(v);
            }
        });


        tvUserFullName= (TextView)v.findViewById(R.id.tv_full_name);
        tvUserFullName.setText(firstName + " " + lastName);

        tvUserInfo= (TextView)v.findViewById(R.id.tv_info_text);
        tvUserInfo.setText(profileDesc);

        tvUserDepartment= (TextView)v.findViewById(R.id.tv_department);
        tvUserDepartment.setText(major);


        userPosts = new ArrayList<Object>();
        rvUserPosts = (RecyclerView)v.findViewById(R.id.rv_user_posts);
        adapter = new PostsAdapter(getContext(), userPosts, "Post");
        rvUserPosts.setAdapter(adapter);
        rvUserPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        getPosts();

        if(ParseUser.getCurrentUser().getObjectId() == getArguments().getString(ARG_USER_ID)){
            ibSendEmail.setVisibility(View.GONE);
//            ibSetting.setVisibility(View.GONE);
//            ibEditInfo.setVisibility(View.GONE);
        }
//        if(ParseUser.getCurrentUser().getObjectId() != getArguments().getString(ARG_USER_ID)){
////            ibSetting.setVisibility(View.GONE);
////            ibEditInfo.setVisibility(View.GONE);
//            ibSendEmail.setVisibility(View.GONE);
//        }

        // Inflate the layout for this fragment
        return v;
    }

    private void changePhotoPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.getMenuInflater().inflate(R.menu.change_photo_menu, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_gallery:
                        Toast.makeText(getContext(), "edit acc", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_camera:

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
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
                    Log.i("Test_test", profile.getFirstName() + " " + profile.getGraduationYear());
                    firstName = profile.getFirstName();
                    lastName = profile.getLastName();
                    profileDesc = profile.getProfileDesc();
                    major = profile.getMajor();
                    Log.i("GRAD_DATE", "Year: " + profile.getGraduationYear() );
                    Log.i("GRAD_DATE", "Month: " + profile.getGraduationMonth() );
                    gradYear = String.valueOf(profile.getGraduationYear()) ;
                    gradMonth = String.valueOf(profile.getGraduationMonth());
                    profileImg = (profile.getProfileImg().getUrl() == null) ? getProfileURL(temp_user.getObjectId()): profile.getProfileImg().getUrl();
                    coverImg = null;
                }
                updateView();
            } else {
                //Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Test","Profile not found.");
            }
        });
    }

    private String getProfileURL(String userId){
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    private void updateView(){
        tvUserFullName.setText(firstName + " " + lastName);
        tvUserInfo.setText(profileDesc);
        tvUserDepartment.setText(major);

        if(gradYear == null || gradMonth == null){
            tvGradDate.setText("TBD");
        }else {
            tvGradDate.setText(gradMonth + ", " + gradYear);
        }


        Log.i("Profile_img","Provided Profile_img: "+  profileImg);

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

    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.getMenuInflater().inflate(R.menu.setting_popup, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_acc:
                        Toast.makeText(getContext(), "edit acc", Toast.LENGTH_SHORT).show();
                        EditAccountDialog editAccountDialog = new EditAccountDialog(ParseUser.getCurrentUser().getObjectId().toString());
                        editAccountDialog.show(getActivity().getSupportFragmentManager(), "Example dialog");
                        break;
                    case R.id.logout:
                        Toast.makeText(getContext(), "logout", Toast.LENGTH_SHORT).show();
                        ParseUser.logOut();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public static class EditProfileDialog extends AppCompatDialogFragment{
        private String userId;
        EditText et_firstName,
                et_lastName,
                et_gradYear,
                et_gradMonth,
                et_major,
                et_description;

        public EditProfileDialog(String userId){
            this.userId = userId;
        }


        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.edit_dialog);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_edit_profile, null);

            et_firstName = (EditText)view.findViewById(R.id.et_edit_firstName);
            et_lastName  = (EditText)view.findViewById(R.id.et_edit_lastName);
            et_gradYear = (EditText)view.findViewById(R.id.et_edit_gradYear);
            et_gradMonth = (EditText)view.findViewById(R.id.et_edit_gradMonth);
            et_major = (EditText)view.findViewById(R.id.et_edit_major);
            et_description = (EditText)view.findViewById(R.id.et_edit_description);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
            ParseUser temp_user = new ParseUser();
            temp_user.setObjectId(userId);

            query.whereEqualTo("userId", temp_user);
            query.findInBackground((results, e) -> {
                // do some stuff with results
                if (e == null) {
                    for (ParseObject result : results) {
                        Profile profile = (Profile) result;
                        Log.i("Test", profile.getFirstName() + " " + profile.getLastName());
                        et_firstName.setText(profile.getFirstName());
                        et_lastName.setText(profile.getLastName());
                        et_gradYear.setText(profile.getGraduationYear().toString());
                        et_gradMonth.setText(profile.getGraduationMonth().toString());
                        et_major.setText(profile.getMajor());
                        et_description.setText(profile.getProfileDesc());

                    }
                } else {
                    Log.i("Test","Profile not found.");
                }
            });


            builder.setView(view)
                    .setTitle("Edit Profile")
                    .setIcon(R.drawable.ic_baseline_edit_24)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                            ParseUser temp_user = new ParseUser();
                            temp_user.setObjectId(userId);

                            query.whereEqualTo("userId", temp_user);
                            query.findInBackground((results, e) -> {
                                // do some stuff with results
                                if (e == null) {
                                    for (ParseObject result : results) {
                                        Profile profile = (Profile) result;
                                        Log.i("Test", profile.getFirstName() + " " + profile.getLastName());
                                        result = (Profile)result;
                                        if(!et_firstName.getText().toString().equals("")){
                                            ((Profile) result).setFirstName(et_firstName.getText().toString());
                                        }
                                        if(!et_lastName.getText().toString().equals("")){
                                            ((Profile) result).setLastName(et_lastName.getText().toString());
                                        }

                                        if(et_major.getText().toString().equals("")){
                                            ((Profile) result).setMajor("Undecided");
                                        }else{
                                            ((Profile) result).setMajor(et_major.getText().toString());
                                        }
                                        if (!et_gradYear.getText().toString().equals("")){
                                            ((Profile) result).setGraduationYear(Integer.parseInt(et_gradYear.getText().toString()));
                                        }
                                        if(!et_gradMonth.getText().toString().equals("")){
                                            ((Profile) result).setGraduationMonth(Integer.parseInt(et_gradMonth.getText().toString()));
                                        }

                                        if(!et_description.getText().toString().equals("")){
                                            ((Profile) result).setProfileDesc(et_description.getText().toString());
                                        }else{
                                            ((Profile) result).setProfileDesc(" ");
                                        }
                                        result.saveInBackground();

                                        //Couldnt figure our how to send back to profile with updated info


                                    }
                                } else {
                                    //Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.i("Test","Profile not found.");
                                }
                            });
                        }
                    });

            return builder.create();
        }
    }

    public static class EditAccountDialog extends AppCompatDialogFragment{
        private String userId;
        private boolean passwordIsSame;

        EditText et_email, et_password, et_rePassword;

        public EditAccountDialog(String userId){
            this.userId = userId;
            this.passwordIsSame = false;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.edit_dialog);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_edit_account, null);

            et_email = (EditText)view.findViewById(R.id.et_email);
            et_password = (EditText)view.findViewById(R.id.et_password);
            et_rePassword = (EditText)view.findViewById(R.id.et_rePassword);



            et_email.setText(ParseUser.getCurrentUser().getUsername());
            et_password.setText("");
            et_rePassword.setText("");

//            et_rePassword.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    Log.i("onTextChanged", s.toString());
//                    Log.i("onTextChanged", "et_password: " + et_password.getText().toString());
//                    if(s.toString() == "enterNewPassword"){
//                        Log.i("onTextChanged", "Password is the same");
//                    }
//
//                    if(s.toString() == et_password.getText().toString()){
//                        Log.i("onTextChanged", "Success");
//                        passwordIsSame = true;
//                    }else{
//                        passwordIsSame = false;
//                    }
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if(s.toString() == et_password.getText().toString()){
//                        Log.i("afterTextChanged", "Success");
//                        passwordIsSame = true;
//                    }else{
//                        passwordIsSame = false;
//                    }
//                }
//            });
            Log.i("et_email", String.valueOf(ParseUser.getCurrentUser().getUsername()));

            builder.setView(view)
                    .setTitle("Edit Account Info")
                    .setIcon(R.drawable.ic_baseline_edit_24)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pass = et_password.getText().toString();
                            String rePass = et_rePassword.getText().toString();

                            if (pass.equals(rePass) && !pass.equals("")){
                                ParseUser.getCurrentUser().setPassword(pass);
                                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        Log.i("New Password", "New Password: " + pass);
                                    }
                                });

                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }else{
                                Log.i("New Password", "Password not changed.");
                            }
                            return;
                        }
                    });

            return builder.create();
        }
    }
}