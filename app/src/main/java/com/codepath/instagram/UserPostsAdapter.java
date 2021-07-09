package com.codepath.instagram;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

public class UserPostsAdapter extends RecyclerView.Adapter<UserPostsAdapter.ViewHolder> {
    Context mContext;
    List<Post> mPosts;

    public UserPostsAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
    }

    @NonNull
    @Override
    public UserPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPostsAdapter.ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.ivUserPost);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("PA", "item clicked");
                    Toast.makeText(mContext, "item clicked", Toast.LENGTH_LONG).show();
                    //get item position
                    int position = getAdapterPosition();
                    //get Movie at this position
                    Post post = mPosts.get(position);
                    // Create Intent for the new activity
                    Intent i = new Intent(mContext, DetailsPost.class);
                    // serialize the movie using parceler
                    i.putExtra("post", (Serializable) post);
                    //show the activity
                    mContext.startActivity(i);
                }
            });
        }

        public void bind(Post post) {
            if (post.getImage() != null ) {
                Glide.with(mContext).load(post.getImage().getUrl()).into(mIvImage);
            }
        }

    }
}
