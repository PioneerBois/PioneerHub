package com.pandinu.PioneerHub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pandinu.PioneerHub.fragments.LibraryFragment;
import com.pandinu.PioneerHub.fragments.MapFragment;
import com.pandinu.PioneerHub.fragments.PostFragment;
import com.pandinu.PioneerHub.fragments.ProfileFragment;
import com.pandinu.PioneerHub.fragments.ResourceFragment;
import com.pandinu.PioneerHub.fragments.TimelineFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements TimelineFragment.TimeLineFragmentListener, PostFragment.PostFragmentListener{

    //private FragmentTransaction ft;
    private TimelineFragment timeLineFragment;
    //private PostFragment postFragment;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_resources:
                        fragment = new LibraryFragment();
                        break;
                    case R.id.action_timeline:
                        timeLineFragment = new TimelineFragment(getString(R.string.student_feed));
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, timeLineFragment).commit();
                        //timeLineFragment.queryPost();
                        return true;
                    case R.id.action_profile:
                        ParseUser user = ParseUser.getCurrentUser();
                        Log.i("Test", user.getObjectId() + " : this is the user id");

                        fragment = ProfileFragment.newInstance(user.getObjectId());
                        break;
                    default:
                        fragment = new MapFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_timeline);



        /*timeLineFragment = new TimelineFragment(getString(R.string.student_feed));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, timeLineFragment);
        ft.commit();
        timeLineFragment.queryPost(getString(R.string.student_feed));*/






    }

    @Override
    public void toPostSent(String channel) {
        FragmentTransaction toPostTransaction = getSupportFragmentManager().beginTransaction();
        PostFragment fragment = PostFragment.newInstance(channel);
        toPostTransaction.replace(R.id.container, fragment).addToBackStack("null");
        toPostTransaction.commit();
    }

    @Override
    public void cancelPost(String channel) {
        getSupportFragmentManager().popBackStack();
        FragmentTransaction cancelPost = getSupportFragmentManager().beginTransaction();
        cancelPost.replace(R.id.container, timeLineFragment);
        cancelPost.commit();
    }

    @Override
    public void successfulPost(String channel) {
        getSupportFragmentManager().popBackStack();
        FragmentTransaction successfulPost = getSupportFragmentManager().beginTransaction();
        //TimelineFragment fragment = new TimelineFragment(channel);
        successfulPost.replace(R.id.container, timeLineFragment);
        successfulPost.commit();
        //timeLineFragment.queryPost(channel);
        //timeLineFragment.setToolbarTitle();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        Log.i("count", String.valueOf(count));
        if (count == 0) {
            super.onBackPressed();

            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}