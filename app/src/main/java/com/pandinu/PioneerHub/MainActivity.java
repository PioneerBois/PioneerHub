package com.pandinu.PioneerHub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.pandinu.PioneerHub.Model.User;

public class MainActivity extends AppCompatActivity implements TimelineFragment.TimeLineFragmentListener, PostFragment.PostFragmentListener{

    //private FragmentTransaction ft;
    //private TimelineFragment timeLineFragment;
    //private PostFragment postFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //timeLineFragment = new TimelineFragment();
        //postFragment = new PostFragment();

        //This will pass a serializable user object in the future
        ProfileFragment profileFragment = ProfileFragment.newInstance(
                "Harry Potter",
                "Im a hairy wizard, Hagrid!",
                "Cupboard underneath the stairs",
                "https://st3.depositphotos.com/15648834/17930/v/1600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg",
                "https://external-preview.redd.it/uhq5zTcMPM3tOW_fbUz4PayDt_5pkEXdyXXoRWs3XOg.jpg?width=960&crop=smart&auto=webp&s=1a732c9516521dd48e60bf092ab49df8d291e744"
                );


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //TimelineFragment fragment = TimelineFragment.newInstance(getString(R.string.student_feed));
        //ft.replace(R.id.container, fragment);
        ft.replace(R.id.container, profileFragment);
        ft.commit();

    }

    @Override
    public void toPostSent(String channel) {
        FragmentTransaction toPostTransaction = getSupportFragmentManager().beginTransaction();
        PostFragment fragment = PostFragment.newInstance(channel);
        toPostTransaction.replace(R.id.container, fragment);
        toPostTransaction.commit();
    }

    @Override
    public void cancelPost(String channel) {
        FragmentTransaction cancelPost = getSupportFragmentManager().beginTransaction();
        TimelineFragment fragment = TimelineFragment.newInstance(channel);
        cancelPost.replace(R.id.container, fragment);
        cancelPost.commit();
    }

    @Override
    public void successfulPost(String channel) {
        FragmentTransaction successfulPost = getSupportFragmentManager().beginTransaction();
        TimelineFragment fragment = TimelineFragment.newInstance(channel);
        successfulPost.replace(R.id.container, fragment);
        successfulPost.commit();

    }
}