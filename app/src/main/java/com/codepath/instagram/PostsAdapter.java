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

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    Context mContext;
    List<Post> mPosts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//            implements View.OnClickListener {
        TextView mTvUsername;
        TextView mTvDescription;
        ImageView mIvImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvDescription = itemView.findViewById(R.id.tvDescription);
            mTvUsername = itemView.findViewById(R.id.tvUsername);
            mIvImage = itemView.findViewById(R.id.ivImage);
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
            mTvUsername.setText(post.getUser().getUsername());
            mTvDescription.setText(post.getDescription());
            if (post.getImage() != null ) {
                Glide.with(mContext).load(post.getImage().getUrl()).into(mIvImage);
            }
        }

    }
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
