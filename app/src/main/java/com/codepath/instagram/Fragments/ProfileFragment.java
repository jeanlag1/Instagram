package com.codepath.instagram.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.instagram.Post;
import com.codepath.instagram.PostsAdapter;
import com.codepath.instagram.R;
import com.codepath.instagram.UserPostsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private TextView mUsername;
    private RecyclerView mGrid;
    private ImageView mProfileImg;
    private Button mEdit;
    private List<Post> mPosts;
    private UserPostsAdapter mAdapter;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEdit = view.findViewById(R.id.btnEditProfile);
        mGrid = view.findViewById(R.id.rvUserPosts);
        mProfileImg = view.findViewById(R.id.ivProfile);
        mUsername = view.findViewById(R.id.tvProfileUsername);

        mUsername.setText(ParseUser.getCurrentUser().getUsername());

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Edit Profile image", Toast.LENGTH_LONG ).show();
            }
        });

        mPosts = new ArrayList<>();
        mAdapter = new UserPostsAdapter(getContext(),mPosts);
        mGrid.setAdapter(mAdapter);
        mGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));
        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        // get the latest 20 messages, order will show up newest to oldest of this group
        query.orderByAscending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                Log.i(TAG, "Im here");
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                // save received posts to list and notify adapter of new data
                mPosts.addAll(posts);
                mAdapter.notifyDataSetChanged();
            }
        });

    }
}