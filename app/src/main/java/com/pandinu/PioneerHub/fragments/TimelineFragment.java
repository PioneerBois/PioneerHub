package com.pandinu.PioneerHub.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.pandinu.PioneerHub.Post;
import com.pandinu.PioneerHub.PostsAdapter;
import com.pandinu.PioneerHub.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class TimelineFragment extends Fragment implements PostFragment.PostFragmentListener{
    private static final String TAG = "TimeLineFragment";
    private static final String SAVED_RECYCLER_VIEW_DATASET_ID = "DATASET_ID";
    private static final String SAVED_RECYCLER_VIEW_STATUS_ID = "STATUS_ID";
    //private TimeLineFragmentListener listener;
    private TextView tvToPost;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton fabToPost;
    private static final String ARG_CHANNEL = "";
    private String currentChannel = "";
    private ArrayList<Post> allPosts;
    private RecyclerView rvPosts;
    private PostsAdapter adapter;
    private FrameLayout childFrameLayout;
    private RelativeLayout rlContent;
    private PostFragment postFragment;
    private Parcelable mListState;



    public TimelineFragment() {

    }

    public static TimelineFragment newInstance(String channel) {

        Bundle args = new Bundle();

        TimelineFragment fragment = new TimelineFragment();
        args.putString(ARG_CHANNEL, channel);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_time_line, container, false);

        if(getArguments() != null){
            currentChannel = getArguments().getString(ARG_CHANNEL);
            //Log.i(TAG, "getargments is not null " + currentChannel);
        }

        childFrameLayout = v.findViewById(R.id.child_fragment_container);
        if(childFrameLayout!=null) {
            Log.i(TAG, "childFramelayout made");
        }

        mDrawer = (DrawerLayout) v.findViewById(R.id.drawer_layout);

        rlContent = v.findViewById(R.id.rlContent);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(currentChannel);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        // Find our drawer view
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        nvDrawer = (NavigationView) v.findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        nvDrawer.setCheckedItem(R.id.studentFeed);

        fabToPost = v.findViewById(R.id.fabToPost);
        fabToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener.toPostSent(currentChannel);

                insertNestedFragment();
            }
        });


        /*If the timelinefragment was recreated by orientation change
        *       If shown the child Post Fragment before rotation insert and show the PostFragment above the timelinefragment
        *       If shown the timelinefragment before rotation just create new Post fragment instance with the channel that was saved
        *   Either case restore the timeline fragment recyclerview with the position and post that the user has queried
        *   before the orientation
        * else
        *   Create new post fragment and set up recyclerview and query the post in the database
        * */

        if(savedInstanceState != null){
            int stateValue = savedInstanceState.getInt("visible");
            currentChannel = savedInstanceState.getString("channel");
            //childFrameLayout = v.findViewById(R.id.child_fragment_container);
            if(stateValue == 1){
                Log.i(TAG, "Rotated the device and PostFragment is shown");
                postFragment = (PostFragment) getChildFragmentManager().getFragment(savedInstanceState, "postFragment");
                insertNestedFragment();
                //return v;
            }else{
                Log.i(TAG, "Rotated the device and TimeLineFragment is shown");
                postFragment = PostFragment.newInstance(currentChannel);
                toolbar.setTitle(currentChannel);
            }
            //Log.i(TAG, "savedInstance is not null");

            rvPosts = v.findViewById(R.id.rvPosts);

            allPosts = savedInstanceState.getParcelableArrayList(SAVED_RECYCLER_VIEW_DATASET_ID);
            //Log.i(TAG, "length of all Posts: " + String.valueOf(allPosts.size()));
            adapter = new PostsAdapter(getContext(), allPosts);
            rvPosts.setAdapter(adapter);
            rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));


            mListState = savedInstanceState.getParcelable(SAVED_RECYCLER_VIEW_STATUS_ID);

            if (mListState!= null && rvPosts != null) {
                //Parcelable listState = savedInstanceState.getParcelable(SAVED_RECYCLER_VIEW_STATUS_ID);
                if (rvPosts.getLayoutManager() != null) {
                    rvPosts.getLayoutManager().onRestoreInstanceState(mListState);
                    Log.i(TAG, "restored the list state");
                }else{
                   // Log.i(TAG, "rvPosts.getLayoutManager() null");
                }
            }else{
               // Log.i(TAG, "mliststate or rvPost is null");
            }


        }else{
            postFragment = PostFragment.newInstance(currentChannel);
            allPosts = new ArrayList<>();
            rvPosts = v.findViewById(R.id.rvPosts);
            adapter = new PostsAdapter(getContext(), allPosts);
            rvPosts.setAdapter(adapter);
            rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

            queryPost();
        }

        return v;
    }



    //Set up the channels to be clicked on

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

    //Select a new drawer item pick between what the different channels are

    private void selectDrawerItem(MenuItem menuItem) {

        if(postFragment != null){
            getChildFragmentManager().popBackStack();
        }

        switch(menuItem.getItemId()) {
            case R.id.studentFeed:
                currentChannel = getString(R.string.student_feed);
                postFragment = PostFragment.newInstance(currentChannel);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.buyAndSell:
                currentChannel= getString(R.string.buy_and_sell);
                postFragment = PostFragment.newInstance(currentChannel);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.lostAndFound:
                currentChannel= getString(R.string.lost_and_found);
                postFragment = PostFragment.newInstance(currentChannel);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.housing:
                currentChannel= getString(R.string.housing);
                postFragment = PostFragment.newInstance(currentChannel);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.news:
                currentChannel= getString(R.string.news);
                postFragment = PostFragment.newInstance(currentChannel);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.rideSharing:
                currentChannel= getString(R.string.ride_sharing);
                postFragment = PostFragment.newInstance(currentChannel);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            default:
                currentChannel = getString(R.string.student_feed);
                postFragment = PostFragment.newInstance(currentChannel);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
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

    private void insertNestedFragment() {
        //postFragment = PostFragment.newInstance(currentChannel);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, postFragment).addToBackStack("null").commit();

        childFrameLayout.setVisibility(View.VISIBLE);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    /*
        When you cancel a post you want to pop the fragment and hide the container that holds the fragment
        Unlock the drawer now to be pulled and set the title back to the correct feed name
     */

    @Override
    public void cancelPost(String channel) {

        getChildFragmentManager().popBackStack();
        hideChildFrameLayout();
        mDrawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED);
        currentChannel = channel;
        toolbar.setTitle(currentChannel);


        Log.i(TAG, "cancelPost");
       // drawerToggle.syncState();
        //requestFocus();


    }

    //Similar to cancelPost but now we wnat to query for new posts

    @Override
    public void successfulPost(String channel) {
        getChildFragmentManager().popBackStack();
        hideChildFrameLayout();
        currentChannel = channel;
        mDrawer.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED);
        queryForNewPost(channel);
    }


    //hide the child framelayout that holds the child fragment from view
    public int hideChildFrameLayout(){
        //childFrameLayout = v.
        if(childFrameLayout!=null) {
            //childFrameLayout.setVisibility(View.GONE);
            Log.i(TAG, "child framelayout is not null hide child framelayout");
            childFrameLayout.setVisibility(View.GONE);
            return 1;
        }else{
            Log.i(TAG, "child framelayout is null when trying to hide child framelayout");
            return 0;

        }



        //drawerToggle.syncState();
    }

    //Query the post in the database and order than from most recent to least by the createdAt date
    public void queryPost(){
        //currentChannel = channel;
        Log.i(TAG, "queryPost");

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        if(currentChannel != getString(R.string.student_feed)){
            query.whereEqualTo(Post.KEY_CHANNEL, currentChannel);
        }
        query.orderByDescending(Post.KEY_CREATEDAT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e!=null){
                    Log.e(TAG,"Issue with getting posts", e);
                    return;
                }

                /*for(Post post: posts){
                    Log.i(TAG,"Post: " + post.getDescription());
                }*/

                allPosts.clear();
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });


    }

    //Query for new posts where each post are more rescent than the already top post (at position 0 of the recyclerview )

    private void queryForNewPost(String channel) {
        Log.i(TAG, "queryForNewPost");
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereGreaterThan(Post.KEY_CREATEDAT, allPosts.get(0).getPostCreatedAt());
        query.orderByDescending(Post.KEY_CREATEDAT);
        query.whereEqualTo(Post.KEY_CHANNEL, currentChannel);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e!=null){
                    Log.i(TAG, "Issue with getting new Post", e);
                    return;
                }


                //Log.i(TAG, "Getting back how many posts: " + String.valueOf(posts.size()));
                for(Post post:  posts){
                    allPosts.add(0, post);
                    //adapter.notifyItemChanged(0);
                }

                adapter.notifyDataSetChanged();

               //Log.i("TAG", "The post at position 0 description: " + allPosts.get(0).getDescription());
            }
        });

    }


    //Save the recyclerview position

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        mListState = rvPosts.getLayoutManager().onSaveInstanceState();
        //save recyclerview position
        outState.putParcelable(SAVED_RECYCLER_VIEW_STATUS_ID, mListState);

        //save recycler items
        outState.putParcelableArrayList(SAVED_RECYCLER_VIEW_DATASET_ID, allPosts);

        //save the channel
        outState.putString("channel", currentChannel);

        if (childFrameLayout.getVisibility() == View.VISIBLE) {
            outState.putInt("visible", 1);
            getChildFragmentManager().putFragment(outState, "postFragment", postFragment);

            Log.i(TAG, "Post Fragment is visible");
        }else{
            outState.putInt("visible", 0);
            //getChildFragmentManager().putFragment(outState, "postFragment", postFragment);
            Log.i(TAG, "Post Fragment is not visible");
        }
    }


}