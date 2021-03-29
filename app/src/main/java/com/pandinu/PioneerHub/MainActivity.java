package com.pandinu.PioneerHub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements TimelineFragment.TimeLineFragmentListener, PostFragment.PostFragmentListener{

    private FragmentTransaction ft;
    private TimelineFragment timeLineFragment;
    //private PostFragment postFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeLineFragment = new TimelineFragment();
        //postFragment = new PostFragment();


        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, timeLineFragment);
        ft.commit();

    }

    @Override
    public void toPostSent() {
        FragmentTransaction toPostTransaction = getSupportFragmentManager().beginTransaction();
        toPostTransaction.replace(R.id.container, new PostFragment());
        toPostTransaction.commit();
    }

    @Override
    public void cancelPost() {
        FragmentTransaction cancelPost = getSupportFragmentManager().beginTransaction();
        cancelPost.replace(R.id.container, timeLineFragment);
        cancelPost.commit();
    }

    @Override
    public void successfulPost() {
        FragmentTransaction successfulPost = getSupportFragmentManager().beginTransaction();
        successfulPost.replace(R.id.container, timeLineFragment);
        successfulPost.commit();
    }
}