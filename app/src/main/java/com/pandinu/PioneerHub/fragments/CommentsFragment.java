package com.pandinu.PioneerHub.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pandinu.PioneerHub.Comments;
import com.pandinu.PioneerHub.Post;
import com.pandinu.PioneerHub.PostsAdapter;
import com.pandinu.PioneerHub.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String TAG = "CommentsFragment";
    //private static final String ARG_PARAM2 = "param2";

    private ArrayList<Object> items;
    private RecyclerView rvItems;
    private PostsAdapter adapter;
    private Toolbar toolbar;
    private EditText etComments;
    private FloatingActionButton fabToComment;
    private int timelinePostPosition;
    private Fragment timeLineFragment;

    // TODO: Rename and change types of parameters
    private Post post;

    public CommentsFragment() {
        // Required empty public constructor
    }

    private CommentFragmentListener listener;

    public interface CommentFragmentListener{
        void successfulComment(int timelinePostPosition);
    }


    // TODO: Rename and change types and number of parameters
    public static CommentsFragment newInstance(Post post, int timelinePostPosition) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1,  post);
        args.putInt(ARG_PARAM2,  timelinePostPosition);
        //args.putSerializable(ARG_PARAM3, (Serializable) timeLineFragment);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable(ARG_PARAM1);
            timelinePostPosition = getArguments().getInt(ARG_PARAM2);
            //timeLineFragment = (Fragment) getArguments().getSerializable(ARG_PARAM3);
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

        etComments = v.findViewById(R.id.etComments);

        fabToComment = v.findViewById(R.id.fabToComment);
        fabToComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentToPost();
            }
        });


        queryComments(post);




        return v;
    }

    private void addCommentToPost() {
        String description = etComments.getText().toString().trim();
        if(description.isEmpty()){
            Toast.makeText(getActivity(), "The comment is Empty", Toast.LENGTH_SHORT);
            return;
        }

        Comments comment = new Comments();
        String postObjectId = post.getPostObjectId();
        ParseUser userId = ParseUser.getCurrentUser();
        comment.setRepliedPostId(ParseObject.createWithoutData("Post", postObjectId));
        comment.setUserId(userId);
        comment.setDescription(description);



        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.i(TAG, "Issue with saving comment", e);
                    return;
                }

                etComments.setText("");

                hideKeyBoard();

                queryForNewComment();
                listener.successfulComment(timelinePostPosition);

                //items.add(1, comment);
                //listener.successfulComment(timelinePostPosition);


            }
        });



        //ParseQuery<Comments> query = ParseQuery.getQuery("Comments");

    }

    private void hideKeyBoard() {
        /*View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }*/

        final InputMethodManager imm = (InputMethodManager) etComments.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etComments.getWindowToken(), 0);

        Log.i(TAG, "hidekeyboard");
    }

    private void  queryForNewComment() {
        ParseQuery<Comments> query = ParseQuery.getQuery(Comments.class);
        String postObjectId = post.getPostObjectId();
        ParseUser userId = ParseUser.getCurrentUser();
        query.whereEqualTo(Comments.KEY_REPLIEDPOSTID,ParseObject.createWithoutData("Post", postObjectId));
        query.whereEqualTo(Comments.KEY_USERID, userId);





        if(items.size() > 2) {
            Comments comment = (Comments) items.get(1);
            query.whereGreaterThan(Post.KEY_CREATEDAT, comment.getCommentCreatedAt());

            query.findInBackground(new FindCallback<Comments>() {
                @Override
                public void done(List<Comments> comments, ParseException e) {
                    if (e != null) {
                        Log.i(TAG, "Issue with updating comments", e);
                    }

                    for (Comments comment : comments) {
                        items.add(1, comment);
                    }

                    rvItems.scrollToPosition(0);
                    adapter.notifyDataSetChanged();

                }
            });
        }else{
            queryComments(post);
        }


    }

    private void queryComments(Post post) {

        ParseQuery<Comments> query = ParseQuery.getQuery(Comments.class);
        String postObjectId = post.getPostObjectId();
        query.whereEqualTo(Comments.KEY_REPLIEDPOSTID,ParseObject.createWithoutData("Post", postObjectId));
        query.orderByAscending(Comments.KEY_CREATEDAT);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            listener = (CommentFragmentListener) getTargetFragment();
            if(listener != null){
                Log.i(TAG, "listener is not null");
            }else{
                Log.i(TAG, "listener is null");
            }
        }catch (ClassCastException e){
            Log.e(TAG,"onAttach: ClassException", e);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}