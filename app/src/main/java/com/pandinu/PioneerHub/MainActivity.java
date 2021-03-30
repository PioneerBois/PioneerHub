package com.pandinu.PioneerHub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

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


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        TimelineFragment fragment = TimelineFragment.newInstance(getString(R.string.student_feed));
        ft.replace(R.id.container, fragment);
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