package com.pandinu.PioneerHub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pandinu.PioneerHub.fragments.LibraryFragment;
import com.pandinu.PioneerHub.fragments.MapFragment;
import com.pandinu.PioneerHub.fragments.PostFragment;
import com.pandinu.PioneerHub.fragments.ProfileFragment;
import com.pandinu.PioneerHub.fragments.ResourceFragment;
import com.pandinu.PioneerHub.fragments.TimelineFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //private FragmentTransaction ft;
    private TimelineFragment timeLineFragment;

    private LibraryFragment libraryFragment;
    //private PostFragment postFragment;

    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String channel  = getString(R.string.student_feed);
        timeLineFragment = TimelineFragment.newInstance(channel);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_resources:
                        fragment = libraryFragment.newInstance();
                        break;
                    case R.id.action_timeline:
                        //String channel  = getString(R.string.student_feed);
                        //timeLineFragment = TimelineFragment.newInstance(channel);

                        getSupportFragmentManager().beginTransaction().replace(R.id.container, timeLineFragment).commit();
                        //timeLineFragment.queryPost();
                        return true;
                    case R.id.action_profile:
                        fragment = ProfileFragment.newInstance(
                                "Test Test",
                                "this is a test",
                                "Department",
                                "test",
                                "test"
                                );
                        break;
                    default:
                        fragment = MapFragment.newInstance();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            }
        });

        //bottomNavigationView.setSelectedItemId(R.id.action_timeline);

        if(savedInstanceState ==null) {
            bottomNavigationView.setSelectedItemId(R.id.action_timeline);
        }
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    if(frag instanceof TimelineFragment){
                        int hidChildFrameLayout = timeLineFragment.hideChildFrameLayout();
                        if(hidChildFrameLayout == 0){
                            finish();
                        }
                    }
                    //finish();
                    return;
                }
            }
        }

        super.onBackPressed();


    }
}