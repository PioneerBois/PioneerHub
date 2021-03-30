package com.pandinu.PioneerHub;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class TimelineFragment extends Fragment {
    private TimeLineFragmentListener listener;
    private TextView tvToPost;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton fabToPost;
    private static final String ARG_CHANNEL = "";
    private String currentChannel = "";

    public interface TimeLineFragmentListener{
        void toPostSent(String channel);
    }
    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static TimelineFragment newInstance(String channel) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHANNEL, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_time_line, container, false);

        if(getArguments() != null){
            //Log.i("getArguments" , "getArguments");
            currentChannel = getArguments().getString(ARG_CHANNEL);
        }

        Log.i("currentChannel", currentChannel);
        //Log.i("currentChannel", currentChannel);


        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        nvDrawer = (NavigationView) v.findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        setDrawerToCurrentChannel();

        //drawerToggle = ((AppCompatActivity)getActivity()).setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        //((AppCompatActivity)getActivity()) drawerToogle.setDrawerIndicatorEnabled(true);

        tvToPost = v.findViewById(R.id.tvToPost);
        tvToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.toPostSent(currentChannel);
            }
        });

        fabToPost = v.findViewById(R.id.fabToPost);
        fabToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.toPostSent(currentChannel);
            }
        });

        //Log.i("getcalled", "getcallled");

        return v;
    }

    private void setDrawerToCurrentChannel() {
        if(currentChannel.equals(getString(R.string.student_feed))){
            nvDrawer.setCheckedItem(R.id.studentFeed);
        }else if(currentChannel.equals(getString(R.string.buy_and_sell))){
            nvDrawer.setCheckedItem(R.id.buyAndSell);
        }else if(currentChannel.equals(getString(R.string.lost_and_found))){
            nvDrawer.setCheckedItem(R.id.lostAndFound);
        }else if(currentChannel.equals(getString(R.string.housing))){
            nvDrawer.setCheckedItem(R.id.housing);
        }else if(currentChannel.equals(getString(R.string.news))){
            nvDrawer.setCheckedItem(R.id.news);
        }else if(currentChannel.equals(getString(R.string.ride_sharing))){
            nvDrawer.setCheckedItem(R.id.rideSharing);
        }else{
            nvDrawer.setCheckedItem(R.id.studentFeed);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.studentFeed:
                currentChannel = getString(R.string.student_feed);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                mDrawer.closeDrawers();
                break;
            case R.id.buyAndSell:
                currentChannel= getString(R.string.buy_and_sell);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                mDrawer.closeDrawers();
                break;
            case R.id.lostAndFound:
                currentChannel= getString(R.string.lost_and_found);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                mDrawer.closeDrawers();
                break;
            case R.id.housing:
                currentChannel= getString(R.string.housing);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                mDrawer.closeDrawers();
                break;
            case R.id.news:
                currentChannel= getString(R.string.news);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                mDrawer.closeDrawers();
                break;
            case R.id.rideSharing:
                currentChannel= getString(R.string.ride_sharing);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                mDrawer.closeDrawers();
                break;
            default:
                currentChannel = getString(R.string.student_feed);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                mDrawer.closeDrawers();
                break;
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(getActivity(), mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof TimeLineFragmentListener){
            listener = (TimeLineFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " " +
                    "must implement TimeLineFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}