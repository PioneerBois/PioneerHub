package com.pandinu.PioneerHub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements TimelineFragment.TimeLineFragmentListener, PostFragment.PostFragmentListener{

    //private FragmentTransaction ft;
    private TimelineFragment timeLineFragment;
    //private PostFragment postFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        timeLineFragment = new TimelineFragment(getString(R.string.student_feed));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, timeLineFragment);
        ft.commit();
        timeLineFragment.queryPost(getString(R.string.student_feed));






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
        cancelPost.replace(R.id.container, timeLineFragment);
        cancelPost.commit();
    }

    @Override
    public void successfulPost(String channel) {
        FragmentTransaction successfulPost = getSupportFragmentManager().beginTransaction();
        //TimelineFragment fragment = new TimelineFragment(channel);
        successfulPost.replace(R.id.container, timeLineFragment);
        successfulPost.commit();
        timeLineFragment.queryPost(channel);
        //timeLineFragment.setToolbarTitle();
    }
}