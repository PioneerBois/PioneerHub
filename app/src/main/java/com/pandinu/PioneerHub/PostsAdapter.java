package com.pandinu.PioneerHub;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pandinu.PioneerHub.fragments.CommentsFragment;
import com.pandinu.PioneerHub.fragments.PreviewImageFragment;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "PostsAdapter";
    private Context context;
    private List<Object> items;
    private boolean userLiked = false;
    private String type;

    private final int POST = 0, COMMENTS = 1;

    public PostsAdapter(Context context, List<Object> items, String type){
        this.context = context;
        this.items = items;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case POST:
                View v1 = inflater.inflate(R.layout.timeline_post, parent, false);
                viewHolder = new PostViewHolder(v1);
                break;
            case COMMENTS:
                View v2 = inflater.inflate(R.layout.post_comments, parent, false);
                viewHolder = new CommentViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        //Post post = posts.get(position);
        //holder.bind(post,position);

        switch (viewHolder.getItemViewType()) {
            case POST:
                PostViewHolder postViewHolder = (PostViewHolder) viewHolder;
                postViewHolder.bind(items,position,type);
                //configureViewHolder1(postViewHolder, position);
                break;
            case COMMENTS:
                CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;
                commentViewHolder.bind(items, position, type);
                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;
        }

    }

    private void configureDefaultViewHolder(RecyclerViewSimpleTextViewHolder vh, int position) {
        vh.getLabel().setText((CharSequence) items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(items.get(position) instanceof Post){
            return POST;
        }else if(items.get(position) instanceof Comments){
            return COMMENTS;
        }

        return -1;
    }

    public class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {

        private TextView label1;


        public RecyclerViewSimpleTextViewHolder(View v) {
            super(v);
            label1 = (TextView) v.findViewById(android.R.id.text1);
        }

        public TextView getLabel() {
            return label1;
        }

        public void setLabel1(TextView label1) {
            this.label1 = label1;
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivProfileImage;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvCommentDescription;
        private ImageView ivLikeIcon;
        private TextView tvLikeText;
        private ImageView ivOptions;
        //private ImageView ivPostImage;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCommentDescription = itemView.findViewById(R.id.tvPostDescription);
            ivOptions = itemView.findViewById(R.id.ivOptions);
            ivLikeIcon = itemView.findViewById(R.id.ivLikeIcon);
            tvLikeText = itemView.findViewById(R.id.tvLikeText);
        }

        public void bind(List<Object> items, int position, String type) {
            Comments comment = (Comments) items.get(position);
            Log.i(TAG, "comment");

            displayProfile(comment);
            displayCommentContent(comment);
            likes(comment, position);
        }

        private void likes(Comments comment, int position) {
            int likesCount = comment.getLikesCount().intValue();
            displayLikes(likesCount);
            boolean userLiked = displayThumbsUp(comment);
            configureLikeButton(comment, userLiked, position);
        }

        private void configureLikeButton(Comments comment, boolean userLiked, int position) {

            ivLikeIcon.setOnClickListener(new View.OnClickListener() {
                //boolean finalUserliked = finalUserLiked;
                @Override
                public void onClick(View v) {
                    //String commentObjectId = comment.getCommentObjectId();

                    if(userLiked){
                        updateComment("removeUserId", comment, position);
                    }else{
                        updateComment("addUserId", comment, position);
                    }
                }
            });

        }

        private void updateComment(String toRemoveOrAddId, Comments comment, int position) {
            String currentUserId = ParseUser.getCurrentUser().getObjectId();
            String commentObjectId = comment.getCommentObjectId();
            ParseQuery<Comments> query = ParseQuery.getQuery("Commments");

            query.whereEqualTo(Comments.KEY_OBJECTID, commentObjectId);
            query.whereEqualTo(Comments.KEY_USERID, currentUserId);

            if(toRemoveOrAddId.equals("removeUserId")){
                comment.removeAll(Post.KEY_LIKESARRAY, Arrays.asList(currentUserId));
                comment.increment(Post.KEY_LIKESCOUNT, -1);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Log.e(TAG, "Issue with removing userId to comments likesArray", e);
                        }

                        queryAndInsertUpdatedComment(position, commentObjectId);
                    }
                });
            }else{
                comment.addAll(Post.KEY_LIKESARRAY, Arrays.asList(currentUserId));
                comment.increment(Post.KEY_LIKESCOUNT, 1);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Log.e(TAG, "Issue with adding userId to likesArray", e);
                        }
                        queryAndInsertUpdatedComment(position, commentObjectId);
                    }
                });

            }
        }



        private void queryAndInsertUpdatedComment(int position, String commentObjectId) {
            ParseQuery<Comments> query = ParseQuery.getQuery("Comments");
            query.getInBackground(commentObjectId, new GetCallback<Comments>() {
                @Override
                public void done(Comments comment, ParseException e) {
                    if(e!=null){
                        Log.i(TAG, "Issue in updated likes in comment", e);
                        return;
                    }
                    Log.i(TAG, "Position of item: " + position);
                    items.set(position, comment);
                    notifyItemChanged(position);
                }
            });
        }

        private boolean displayThumbsUp(Comments comment) {
            String currentUserId = ParseUser.getCurrentUser().getObjectId();

            boolean userLiked = false;
            for(int i = 0; i < comment.getLikesArray().length(); i++) {

                String userId = comment.getLikesArray().optString(i);
                if(userId.equals(currentUserId)){
                    userLiked = true;
                    //ParseUser.getCurrentUser();
                    break;
                }
            }

            if(userLiked){
                ivLikeIcon.setImageResource(R.drawable.ic_baseline_thumb_up_24);
            }else{
                ivLikeIcon.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24);
            }

            return userLiked;
        }

        private void displayLikes(int likesCount) {
            if(likesCount > 0){
                if(likesCount == 1){
                    tvLikeText.setText(likesCount + " " + "like");
                }else{
                    tvLikeText.setText(likesCount + " " + "likes");
                }

                tvLikeText.setVisibility(View.VISIBLE);

            }else{
                tvLikeText.setVisibility(View.GONE);
            }
        }

        private void displayCommentContent(Comments comment) {
            tvCommentDescription.setText(comment.getDescription());
        }

        private void displayProfile(Comments comment) {
            ParseQuery<Profile> query = ParseQuery.getQuery("Profile");
            query.whereEqualTo("userId", comment.getUserId());
            query.findInBackground(new FindCallback<Profile>() {
                @Override
                public void done(List<Profile> objects, ParseException e) {
                    if(e!=null){
                        Log.e(TAG, "Issue with querying Profile");
                        return;
                    }else{
                        //Log.i(TAG, objects.get(0).getFirstName());
                        tvName.setText(objects.get(0).getFirstName() + " " + objects.get(0).getLastName());
                        //tvDate.setText(objects.get(0));

                        //Date date = post.getPostCreatedAt();
                        tvDate.setText( comment.getCommentCreatedAt().toString());
                        Glide.with(context).load(objects.get(0).getProfileImg().getUrl()).into(ivProfileImage);
                    }
                }
            });
        }
    }


    class PostViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivProfileImage;
        private TextView tvName;
        private TextView tvDate;
        private ImageView ivChannelIcon;
        private TextView tvChannelName;
        private TextView tvPostDescription;
        private ImageView ivPostImage;
        private TextView tvLikesNumber;
        private TextView tvCommentsNumber;
        private TextView tvLikeText;
        private TextView tvCommentText;
        private ImageView ivOptions;
        private ImageView ivLikeIcon;
        private ImageView ivCommentIcon;
        private RelativeLayout rlBottomPartPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivChannelIcon = itemView.findViewById(R.id.ivChannelIcon);
            tvChannelName = itemView.findViewById(R.id.tvChannelName);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvLikesNumber = itemView.findViewById(R.id.tvlikesNumber);
            tvCommentsNumber = itemView.findViewById(R.id.tvCommentNumber);
            tvLikeText = itemView.findViewById(R.id.tvLikeText);
            tvCommentText = itemView.findViewById(R.id.tvCommentNumber);
            ivOptions = itemView.findViewById(R.id.ivOptions);
            ivLikeIcon = itemView.findViewById(R.id.ivLikeIcon);
            ivCommentIcon = itemView.findViewById(R.id.ivCommentIcon);
            rlBottomPartPost = itemView.findViewById(R.id.rlBottomPartPost);
        }

        public void bind(Object object, int position, String type) {
            Post post = (Post) items.get(position);
            //Top part of the post
                //Grab the profile data and update the ivProfileImage and tvName and Date;
                //Update channelIcon, and channelName
                //Post Description and check if theire is a postImage
                //display the likes and comments
            //Bottom of the post
                //Display if user liked a post and display that the post could be commented at

            displayProfile(post);
            displayChannelInfo(post);
            displayPostContent(post);
            likes(post, position, type);
            comments(post);

        }

        //Display the comments for now
        private void comments(Post post) {
            int commentsCount = post.getCommentsCount().intValue();
            if(commentsCount > 0){
                tvCommentText.setVisibility(View.VISIBLE);
                if(commentsCount == 1){
                    tvCommentText.setText(commentsCount  + " comment");
                }else {
                    tvCommentText.setText(commentsCount  + " comments");
                }
            }else{
                tvCommentText.setVisibility(View.GONE);
            }

            ivCommentIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                    CommentsFragment commentsFragment = CommentsFragment.newInstance(post);
                    //commentsFragment.setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
                    //commentsFragment.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    //commentsFragment.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                    commentsFragment.show(fm, "");
                    //PreviewImageFragment previewImageFragment = PreviewImageFragment.newInstance(bundle);
                }
            });


        }

         /*Likes function
            handles the number of likes to be shown
            handles what thumbs up to display
            sets up the likes icon clicklistener updates which user likes in Parse and
            updates the new post in the position*/

        private void likes(Post post, int position, String type) {

            int likesCount = post.getLikesCount().intValue();
            displayLikes(likesCount);

            if(type.equals("Post")) {
                rlBottomPartPost.setVisibility(View.VISIBLE);
                boolean userLiked = displayThumbsUp(post);
                configureLikeButton(post, userLiked, position);
            }else{
                rlBottomPartPost.setVisibility(View.GONE);
            }

        }

        //If the user have liked the post and clicks the like button agian, then the user has unliked the post
        //Opposite case and has now liked the post
        private void configureLikeButton(Post post, boolean userLiked, int position) {
            ivLikeIcon.setOnClickListener(new View.OnClickListener() {
                //boolean finalUserliked = finalUserLiked;
                @Override
                public void onClick(View v) {
                   // String postObjectId = post.getPostObjectId();

                    if(userLiked){
                        updatePost("removeUserId", post, position);
                    }else{
                        updatePost("addUserId", post, position);
                    }
                }
            });
        }

        //Check to see if the user has liked the post then display a black thumps up if they did or
        //a white thumbs up if not

        private boolean displayThumbsUp(Post post) {
            String currentUserId = ParseUser.getCurrentUser().getObjectId();

            boolean userLiked = false;
            for(int i = 0; i < post.getLikesArray().length(); i++) {

                String userId = post.getLikesArray().optString(i);
                if(userId.equals(currentUserId)){
                    userLiked = true;
                    //ParseUser.getCurrentUser();
                    break;
                }
            }

            if(userLiked){
                ivLikeIcon.setImageResource(R.drawable.ic_baseline_thumb_up_40);
            }else{
                ivLikeIcon.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_40);
            }

            return userLiked;
        }

        //Display likes for each post

        private void displayLikes(int likesCount) {
            if(likesCount > 0){
                if(likesCount == 1){
                    tvLikesNumber.setText(likesCount + " " + "like");
                }else{
                    tvLikesNumber.setText(likesCount + " " + "likes");
                }

                tvLikesNumber.setVisibility(View.VISIBLE);

            }else{
                tvLikesNumber.setVisibility(View.GONE);
            }
        }

        //Set the new post at the position and notify the adapter the item has changed
        private void queryAndInsertUpdatedPost(int position, String postObjectId) {
            ParseQuery<Post> query = ParseQuery.getQuery("Post");
            query.getInBackground(postObjectId, new GetCallback<Post>() {
                @Override
                public void done(Post post, ParseException e) {
                    if(e!=null){
                        Log.e(TAG, "Issue with querying updated post" , e);
                        return;
                    }
                    items.set(position, post);
                    notifyItemChanged(position);
                }
            });
        }

        //Update the likesArray and save the update in the background
        //Call queryAndInsertUpdatedPost to query insert the newly updated post to the position
        //in the recyclerview

        private void updatePost(String toRemoveOrAddId, Post post , int position) {
            String currentUserId = ParseUser.getCurrentUser().getObjectId();
            ParseQuery<Post> query = ParseQuery.getQuery("Post");
            String postObjectId = post.getPostObjectId();

                if(toRemoveOrAddId.equals("removeUserId")){
                    post.removeAll(Post.KEY_LIKESARRAY, Arrays.asList(currentUserId));
                    post.increment(Post.KEY_LIKESCOUNT, -1);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e!=null){
                                Log.e(TAG, "Issue with removing userId to likesArray", e);
                            }

                            queryAndInsertUpdatedPost(position, postObjectId);
                        }
                    });
                }else{
                    post.addAll(Post.KEY_LIKESARRAY, Arrays.asList(currentUserId));
                    post.increment(Post.KEY_LIKESCOUNT, 1);
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e!=null){
                                Log.e(TAG, "Issue with adding userId to likesArray", e);
                            }
                            queryAndInsertUpdatedPost(position, postObjectId);
                        }
                    });

                }



        }

        //Display the post content
            //If their is no image don't show

        private void displayPostContent(Post post) {
            tvPostDescription.setText(post.getDescription());
            ParseFile image = post.getImg();
            if(image != null){
                Glide.with(context).load(image.getUrl()).into(ivPostImage);
                ivPostImage.setVisibility(View.VISIBLE);
            }else{
                ivPostImage.setVisibility(View.GONE);
            }
        }

        //Display the channel name for the post
        private void displayChannelInfo(Post post) {
            String channel = post.getChannel();
            tvChannelName.setText(channel);
            updateChannelIcon(channel);
        }

        //Display the channel icon
        private void updateChannelIcon(String channel) {
            if(context.getString(R.string.buy_and_sell).equals(channel)){
                ivChannelIcon.setImageResource(R.drawable.ic_baseline_sell_24);
            }else if(context.getString(R.string.lost_and_found).equals(channel)){
                ivChannelIcon.setImageResource(R.drawable.ic_baseline_help_24);
            }else if(context.getString(R.string.housing).equals(channel)){
                ivChannelIcon.setImageResource(R.drawable.ic_baseline_house_24);
            }else if(context.getString(R.string.news).equals(channel)){
                ivChannelIcon.setImageResource(R.drawable.ic_baseline_new_releases_24);
            }else if(context.getString(R.string.ride_sharing).equals(channel)){
                ivChannelIcon.setImageResource(R.drawable.ic_baseline_directions_car_24);
            }else{
                ivChannelIcon.setImageResource(R.drawable.ic_baseline_school_24);
            }

        }

        //query and display the profiles for the user who made the person

        private void displayProfile(Post post) {
            ParseQuery<Profile> query = ParseQuery.getQuery("Profile");
            query.whereEqualTo("userId", post.getUserId());
            query.findInBackground(new FindCallback<Profile>() {
                @Override
                public void done(List<Profile> objects, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with querying Profile");
                        return;
                    } else {
                        //Log.i(TAG, objects.get(0).getFirstName());
                        tvName.setText(objects.get(0).getFirstName() + " " + objects.get(0).getLastName());
                        //tvDate.setText(objects.get(0));

                        //Date date = post.getPostCreatedAt();

                        tvDate.setText(post.getPostCreatedAt().toString());

                        Glide.with(context).load(objects.get(0).getProfileImg().getUrl()).into(ivProfileImage);
                    }
                }
            });

        }
    }
}
