package com.pandinu.PioneerHub;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    private static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;
    private boolean userLiked = false;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timeline_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post,position);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

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

        public ViewHolder(@NonNull View itemView) {
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
        }

        public void bind(Post post, int position) {
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
            likes(post, position);
            displayComments(post);

        }



        private void displayComments(Post post) {
            int commentsCount = post.getCommentsCount().intValue();
            if(commentsCount > 0){
                tvCommentText.setText(commentsCount + ": " + "comments");
            }else{
                tvCommentText.setVisibility(View.GONE);
            }
        }

         /*Likes function
            handles the number of likes to be shown
            handles what thumbs up to display
            sets up the likes icon clicklistener updates which user likes in Parse and
            updates the new post in the position*/

        private void likes(Post post, int position) {

            int likesCount = post.getLikesArray().length();
            displayLikes(likesCount);
            boolean userLiked = displayThumbsUp(post);
            configureLikeButton(post, userLiked, position);

        }

        private void configureLikeButton(Post post, boolean userLiked, int position) {
            ivLikeIcon.setOnClickListener(new View.OnClickListener() {
                //boolean finalUserliked = finalUserLiked;
                @Override
                public void onClick(View v) {
                    String postObjectId = post.getPostObjectId();

                    if(userLiked){
                        updatePost("removeUserId", postObjectId, position);
                    }else{
                        updatePost("addUserId", postObjectId, position);
                    }
                }
            });
        }

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
                    posts.set(position, post);
                    notifyItemChanged(position);
                }
            });
        }

        //Update the likesArray and save the update in the background
        //Call queryAndInsertUpdatedPost to query insert the newly updated post to the position
        //in the recyclerview

        private void updatePost(String toRemoveOrAddId, String postObjectId, int position) {
            String currentUserId = ParseUser.getCurrentUser().getObjectId();
            ParseQuery<Post> query = ParseQuery.getQuery("Post");
            query.getInBackground(postObjectId, new GetCallback<Post>() {
                @Override
                public void done(Post post, ParseException e) {
                    if(e!= null){
                        Log.e(TAG, "Issue with updating post", e);
                        return;
                    }

                    if(toRemoveOrAddId.equals("removeUserId")){
                        post.removeAll(Post.KEY_LIKESARRAY, Arrays.asList(currentUserId));
                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                queryAndInsertUpdatedPost(position, postObjectId);
                            }
                        });
                    }else{
                        post.addAll(Post.KEY_LIKESARRAY, Arrays.asList(currentUserId));
                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                queryAndInsertUpdatedPost(position, postObjectId);
                            }
                        });

                    }

                }
            });

        }


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

        private void displayChannelInfo(Post post) {
            String channel = post.getChannel();
            tvChannelName.setText(channel);
            updateChannelIcon(channel);
        }

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

        private void displayProfile(Post post) {
            ParseQuery<Profile> query = ParseQuery.getQuery("Profile");
            query.whereEqualTo("userId", ParseUser.getCurrentUser());
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
                        tvDate.setText( post.getPostCreatedAt().toString());

                        Glide.with(context).load(objects.get(0).getProfileImg().getUrl()).into(ivProfileImage);
                    }
                }
            });

        }
    }
}
