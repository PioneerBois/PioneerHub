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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    private static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

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
        holder.bind(post);

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
        }

        public void bind(Post post) {
            //Top part of the post
                //Grab the profile data and update the ivProfileImage and tvName and Date;
                //Update channelIcon, and channelName
                //Post Description and check if theire is a postImage
                //Check to see if there are likes and comments > 0

            displayProfile(post);
            displayChannelInfo(post);
            displayPostContent(post);
            displayLikesComemnts(post);

        }

        private void displayLikesComemnts(Post post) {
            int likesCount = post.getLikesCount().intValue();
            int commentsCount = post.getCommentsCount().intValue();
            if(likesCount > 0){
                tvLikesNumber.setText(likesCount + ": " + "likes");
            }else{
                tvLikesNumber.setVisibility(View.GONE);
            }

            if(commentsCount > 0){
                tvCommentText.setText(commentsCount + ": " + "comments");
            }else{
                tvCommentText.setVisibility(View.GONE);
            }
        }

        private void displayPostContent(Post post) {
            tvPostDescription.setText(post.getDescription());
            ParseFile image = post.getImg();
            if(image != null){
                Glide.with(context).load(image.getUrl()).into(ivPostImage);
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
