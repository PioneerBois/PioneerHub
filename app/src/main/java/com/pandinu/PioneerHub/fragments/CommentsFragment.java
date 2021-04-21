package com.pandinu.PioneerHub.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.pandinu.PioneerHub.Comments;
import com.pandinu.PioneerHub.Post;
import com.pandinu.PioneerHub.PostsAdapter;
import com.pandinu.PioneerHub.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "CommentsFragment";
    //private static final String ARG_PARAM2 = "param2";

    private ArrayList<Object> items;
    private RecyclerView rvItems;
    private PostsAdapter adapter;
    private Toolbar toolbar;

    // TODO: Rename and change types of parameters
    private Post post;

    public CommentsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CommentsFragment newInstance(Post post) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,  post);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable(ARG_PARAM1);
            Log.i(TAG,post.getDescription());
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        //getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comments, container, false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        items = new ArrayList<Object>();
        items.add(post);
        rvItems = v.findViewById(R.id.rvItems);
        adapter = new PostsAdapter(getContext(), items, "Comments");
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(getString(R.string.add_a_comment));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        getDialog().setCanceledOnTouchOutside(true);

        queryComments(post);




        return v;
    }

    private void queryComments(Post post) {

        ParseQuery<Comments> query = ParseQuery.getQuery(Comments.class);
        query.orderByDescending(Post.KEY_CREATEDAT);
        query.findInBackground(new FindCallback<Comments>() {
            @Override
            public void done(List<Comments> objects, ParseException e) {
                if(e!=null){
                    Log.i(TAG,"Issue with getting comments", e);
                    return;
                }

                for(Comments comment: objects){
                    items.add(1,comment);
                }

                adapter.notifyDataSetChanged();
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
}