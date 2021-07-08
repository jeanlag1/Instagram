package com.codepath.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class FeedActivity extends AppCompatActivity {

    private static final String TAG = "FeedActivity";
    RecyclerView rvPosts;
    PostsAdapter mAdapter;
    List<Post> mPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        rvPosts = findViewById(R.id.rvFeed);
        mPosts = new ArrayList<>();
        mAdapter = new PostsAdapter(this,mPosts);
        rvPosts.setAdapter(mAdapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        queryPosts();
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // Specify the object id
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issues with getting posts", e );
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + " , username: " + post.getUser().getUsername());
                }
                mPosts.addAll(posts);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}