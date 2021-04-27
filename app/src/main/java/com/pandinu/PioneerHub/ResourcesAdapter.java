package com.pandinu.PioneerHub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pandinu.PioneerHub.Model.Resource;
import com.parse.ParseFile;

import java.util.List;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ViewHolder>{

    private Context context;
    private List<Resource> resources;
    private CardView cardView;

    public ResourcesAdapter(Context context, List<Resource> resources) {
        this.context = context;
        this.resources = resources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resource resource = resources.get(position);
        holder.bind(resource);
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    // Recall: vh represent one item/row
    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivThumbnail;
        private TextView tvName;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void bind(Resource resource) {
            tvName.setText(resource.getName());
            tvDescription.setText(resource.getDescription());

            // We made thumbnail optional, so we check if it exists in the back4app
            ParseFile image = resource.getThumbnail();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivThumbnail);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = resource.getWebsiteUrl();

                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    // Have to add context for getPackageManager() and startActivity() because those are methods of Context.
                    // You can use this method inside an Activity (because an Activity is a Context)
                    // But if youre calling it elsewhere, you need to pass a Context.
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        Log.d("ImplicitIntents", "Can't handle this!");
                    }
                }
            });
        }
    }
}
