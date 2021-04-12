package com.pandinu.PioneerHub.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


public class TimelineFragment extends Fragment {
    private static final String TAG = "TimeLineFragment";
    private TimeLineFragmentListener listener;
    private TextView tvToPost;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private FloatingActionButton fabToPost;
    private static final String ARG_CHANNEL = "";
    private String currentChannel = "";
    private List<Post> allPosts;
    private RecyclerView rvPosts;
    private PostsAdapter adapter;




    public interface TimeLineFragmentListener{
        void toPostSent(String channel);
    }
    public TimelineFragment(String channel) {
        // Required empty public constructor
        currentChannel = channel;
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

        Log.i("currentChannel", currentChannel);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle(currentChannel);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


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





        fabToPost = v.findViewById(R.id.fabToPost);
        fabToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.toPostSent(currentChannel);
            }
        });

        //Log.i("getcalled", "getcallled");

        allPosts = new ArrayList<>();
        rvPosts = v.findViewById(R.id.rvPosts);
        adapter = new PostsAdapter(getContext(), allPosts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        nvDrawer.setCheckedItem(R.id.studentFeed);
        queryPost();


        return v;
    }

    public void queryPost(){
        //currentChannel = channel;
        //Log.i("queryPost", currentChannel);

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

                for(Post post: posts){
                    Log.i(TAG,"Post: " + post.getDescription());
                }

                allPosts.clear();
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });


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
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.buyAndSell:
                currentChannel= getString(R.string.buy_and_sell);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.lostAndFound:
                currentChannel= getString(R.string.lost_and_found);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.housing:
                currentChannel= getString(R.string.housing);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.news:
                currentChannel= getString(R.string.news);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            case R.id.rideSharing:
                currentChannel= getString(R.string.ride_sharing);
                Toast.makeText(getActivity(), currentChannel, Toast.LENGTH_SHORT).show();
                toolbar.setTitle(currentChannel);
                mDrawer.closeDrawers();
                queryPost();
                break;
            default:
                currentChannel = getString(R.string.student_feed);
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